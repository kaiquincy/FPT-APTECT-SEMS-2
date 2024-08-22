package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.Enrollment;
import com.example.demo.service.EnrollmentService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping(value = "/api/enrollment")
@CrossOrigin(origins = "http://localhost:3000") //Cho phep front end su dung API
@Slf4j
public class EnrollmentController {
    @Autowired
    EnrollmentService enrollmentService;

    @PostMapping("{courseId}")
    ApiResponse<Enrollment> postMethodName(@PathVariable Integer courseId) {
        return ApiResponse.<Enrollment>builder().result(enrollmentService.addEnrollment(courseId)).build();
    }

    @GetMapping
    ApiResponse<List<Enrollment>> getMyEnrollments() {
        return ApiResponse.<List<Enrollment>>builder().result(enrollmentService.getMyEnrollments()).build();
    }
    
}
