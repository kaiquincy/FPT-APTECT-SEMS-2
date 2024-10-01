package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Lecture;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    List<Lecture> findAllByCourseId(Integer id);
}
