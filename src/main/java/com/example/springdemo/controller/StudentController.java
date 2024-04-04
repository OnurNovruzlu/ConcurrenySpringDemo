package com.example.springdemo.controller;

import com.example.springdemo.entity.Student;
import com.example.springdemo.service.StudentService;
import com.example.springdemo.util.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("student")
public class StudentController {
    private final StudentService service;

    @GetMapping("/all")
    public ServiceResponse getAllStudents() {
        return service.getALlStudents();
    }

    @PostMapping("/save")
    public ServiceResponse saveStudent(@RequestBody Student body) {
        return service.saveStudent(body);
    }

    @GetMapping("/{id}")
    public ServiceResponse getStudentData(@PathVariable("id") int id) {
        return service.getStudentReportParallel(id);
    }
}
