package com.example.demo.controllers;

import java.util.List;

import com.example.demo.dto.EnrollmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.ApiResponse;
import com.example.demo.service.EnrollmentService;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping(value = "/api/enrollment")
@CrossOrigin(origins = "http://localhost:3000") //Cho phep front end su dung API
@Slf4j
public class EnrollmentController {
    @Autowired
    EnrollmentService enrollmentService;

    @PostMapping("{courseId}")
    ApiResponse<EnrollmentDTO> post(@PathVariable Integer courseId) {
        ApiResponse<EnrollmentDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(enrollmentService.addEnrollment(courseId));
        return apiResponse;

    }

    @PostMapping("{courseId}/update_progress")
    ApiResponse<EnrollmentDTO> updateProgress(@PathVariable Integer courseId) {
        ApiResponse<EnrollmentDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(enrollmentService.updateEnrollment(courseId));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<EnrollmentDTO>> getMyEnrollments() {

        ApiResponse<List<EnrollmentDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(enrollmentService.getMyEnrollments());
        return apiResponse;
    }
    
}
