package com.example.demo.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "courses")
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "TITLE_NULL")
    private String title;

    @NotNull(message = "DESC_NULL")
    private String description;

    @NotNull(message = "PRICE_NULL")
    private Double price;

    @Column(name = "teacher_id")
    private Integer teacherId;

    private double rate;

    @Column(nullable = true)
    private String state;

    @Column(nullable = true)
    private String img;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private String reject_reason;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }



}