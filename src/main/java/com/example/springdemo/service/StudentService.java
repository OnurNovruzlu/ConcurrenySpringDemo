package com.example.springdemo.service;

import com.example.springdemo.dto.StudentReport;
import com.example.springdemo.entity.Student;
import com.example.springdemo.entity.StudentOperation;
import com.example.springdemo.repository.StudentOperationRepository;
import com.example.springdemo.repository.StudentRepository;
import com.example.springdemo.util.ServiceResponse;
import com.example.springdemo.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Array2DHashSet;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository repository;
    private final StudentOperationRepository operationRepository;
    private final ExecutorService executorService;

    public StudentService(StudentRepository repository, StudentOperationRepository operationRepository) {
        this.repository = repository;
        this.operationRepository = operationRepository;
        // THIS IS IMPORTANT (NUMBER OF THREADS)
        this.executorService = Executors.newFixedThreadPool(3);
    }

    public ServiceResponse saveStudent(Student req) {
        Student student;
        if (req.getId() != null && req.getId() > 0) {
            student = repository.findById(req.getId()).orElse(null);
            if (student == null)
                return new ServiceResponse("Can not find student with this id " + req.getId());
        }
        else {
            student = new Student();
            student.setCreatedAt(LocalDateTime.now().withNano(0));
            student.setTimeZone(TimeUtil.setTimezone());
        }

        if (req.getName() != null)
            student.setName(req.getName());
        if (req.getSurname() != null)
            student.setSurname(req.getSurname());
        return new ServiceResponse(repository.saveAndFlush(student));
    }

    public ServiceResponse getALlStudents() {
        return new ServiceResponse(repository.findAll());
    }

    public List<StudentOperation> findStudentOperations(Integer id) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return operationRepository.findByStudentId(id);
    }

    public Optional<Student> findById(Integer id) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return repository.findById(id);
    }

    public ServiceResponse getStudentReportParallel(int id) {
        CompletableFuture<Student> studentFuture = CompletableFuture.supplyAsync(() -> findById(id).orElseThrow(() -> new RuntimeException("Student not found")), executorService);

        CompletableFuture<List<StudentOperation>> operationsFuture = CompletableFuture.supplyAsync(() -> findStudentOperations(id), executorService);
        CompletableFuture<List<StudentOperation>> operationsFuture2 = CompletableFuture.supplyAsync(() -> findStudentOperations(id), executorService);

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(studentFuture, operationsFuture, operationsFuture2);

        try {
            combinedFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        Student student = studentFuture.join();
        List<StudentOperation> operations = operationsFuture.join();
        operations.addAll(operationsFuture2.join());

        StudentReport report = new StudentReport();
        report.setId(student.getId());
        report.setFullName((student.getName() != null ? student.getName() : "") + " " +
                (student.getSurname() != null ? student.getSurname() : ""));
        report.setCreatedAt(student.getCreatedAt());

        if (operations != null) {
            operations.forEach(op -> op.setStudent(null));
            report.setPoints(operations.stream().map(StudentOperation::getPoint).collect(Collectors.toSet()));
            report.setOperations(operations);
        } else {
            report.setPoints(new HashSet<>());
            report.setOperations(new ArrayList<>());
        }

        return new ServiceResponse(report);
    }
}
