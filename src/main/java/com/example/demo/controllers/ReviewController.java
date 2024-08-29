package com.example.demo.controllers;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ReviewDTO;
import com.example.demo.dto.request.ReviewFormRequest;
import com.example.demo.service.IReviewService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/courses")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class ReviewController {

    IReviewService reviewService;

    @GetMapping("/{course-id}/feedbacks")
    ApiResponse<?> getReviews(@PathVariable(name = "course-id") Integer courseId) {
        ApiResponse<List<ReviewDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(reviewService.getAllByCourseId(courseId));
        return apiResponse;
    }

    @PostMapping("/{course-id}/feedbacks")
    ApiResponse<?> createReview(@Valid @RequestBody ReviewFormRequest request, @PathVariable(name = "course-id") Integer courseId) {
        request.setCourseId(courseId);
        ApiResponse<ReviewDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(reviewService.createForCourse(request));
        return apiResponse;
    }
}
