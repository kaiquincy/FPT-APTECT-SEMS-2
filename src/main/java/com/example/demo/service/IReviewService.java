package com.example.demo.service;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.dto.request.ReviewFormRequest;

import java.util.List;

public interface IReviewService {

    List<ReviewDTO> getAllByCourseId(Integer courseId);

    ReviewDTO createForCourse(ReviewFormRequest request);

}
