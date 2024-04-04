package com.example.springdemo.service;

import com.example.springdemo.entity.Student;
import com.example.springdemo.entity.StudentOperation;
import com.example.springdemo.repository.StudentOperationRepository;
import com.example.springdemo.repository.StudentRepository;
import com.example.springdemo.util.ServiceResponse;
import com.example.springdemo.util.TimeUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentOperationService {
    private final StudentOperationRepository repository;
    private final StudentRepository studentRepository;

    public ServiceResponse saveStudentOperation(StudentOperation req) {
        StudentOperation operation;
        if (req.getId() != null && req.getId() > 0) {
            operation = repository.findById(req.getId()).orElse(null);
            if (operation == null)
                return new ServiceResponse("StudentOperation can not find with this id " + req.getId());
        }
        else {
            operation = new StudentOperation();
            operation.setCreatedAt(LocalDateTime.now().withNano(0));
            operation.setTimeZone(TimeUtil.setTimezone());
        }

        if (req.getPoint() == null)
            return new ServiceResponse("Point can not be null");
        operation.setPoint(req.getPoint());

        if (req.getStudent() == null)
            return new ServiceResponse("Student can not be null");
        Student student = studentRepository.findById(req.getStudent().getId()).orElse(null);
        if (student == null)
            return new ServiceResponse("Can not find student with this id " + req.getId());
        operation.setStudent(student);

        return new ServiceResponse(repository.saveAndFlush(operation));
    }

    public ServiceResponse getAllStudentOperations(Integer studentId) {
        List<StudentOperation> operations;
        if (studentId != null && studentId > 0) {
            operations = repository.findByStudentId(studentId);
            operations.forEach(op -> op.setStudent(null));
        } else
            operations = repository.findAll();

        return new ServiceResponse(operations);
    }
}
