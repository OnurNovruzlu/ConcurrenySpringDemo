package com.example.springdemo.dto;

import com.example.springdemo.entity.StudentOperation;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class StudentReport {
    private Integer id;
    private String fullName;
    private LocalDateTime createdAt;
    private Set<Double> points;
    private List<StudentOperation> operations;
}
