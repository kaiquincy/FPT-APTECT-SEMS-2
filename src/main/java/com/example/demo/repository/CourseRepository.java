package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query("SELECT c.id FROM Course c WHERE c.teacherId = :teacherId ORDER BY c.createdAt DESC")
    Optional<List<Integer>> findLatestCourseIdByTeacherId(Integer teacherId);

    List<Course> findAllByTeacherId(Integer id);

    List<Course> findAllByState(String state);
}