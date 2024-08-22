package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Lecture;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    
}
