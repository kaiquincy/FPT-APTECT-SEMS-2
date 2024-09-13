package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long>{
    List<Enrollment> findByStudentId(Integer studentId);
    boolean existsByStudentIdAndCourseId(Integer studentId, Integer courseId);
    Enrollment findByStudentIdAndCourseId(Integer studentId, Integer courseId);
}
