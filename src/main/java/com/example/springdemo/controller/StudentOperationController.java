package com.example.springdemo.controller;

import com.example.springdemo.entity.StudentOperation;
import com.example.springdemo.service.StudentOperationService;
import com.example.springdemo.util.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("student_operation")
public class StudentOperationController {
    private final StudentOperationService service;

    @GetMapping("")
    public ServiceResponse getAllStudentOperations() {
        return service.getAllStudentOperations(null);
    }

    @GetMapping("/{studentId}")
    public ServiceResponse getStudentOperationsByStudent(@PathVariable("studentId") int studentId) {
        return service.getAllStudentOperations(studentId);
    }

    @PostMapping("/save")
    public ServiceResponse saveStudentOperation(@RequestBody StudentOperation body) {
        return service.saveStudentOperation(body);
    }

}
