package com.example.springdemo.repository;

import com.example.springdemo.entity.StudentOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentOperationRepository extends JpaRepository<StudentOperation, Integer> {
    List<StudentOperation> findByStudentId(Integer studentId);
}
