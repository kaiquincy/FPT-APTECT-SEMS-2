package com.example.demo.service.imps;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.dto.request.ReviewFormRequest;
import com.example.demo.entity.Review;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.IReviewService;
import com.example.demo.service.UserService;
import com.example.demo.utils.ArrayUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewService implements IReviewService {

    ReviewRepository reviewRepository;
    ModelMapper modelMapper;
    UserService userService;

    @Override
    public List<ReviewDTO> getAllByCourseId(Integer courseId) {
        List<Review> reviews = reviewRepository.findByCourseId(courseId);
        if (ArrayUtil.isEmpty(reviews))
            return Collections.emptyList();
        return reviews.stream().map(review ->
                modelMapper.map(review, ReviewDTO.class)).toList();
    }

    @Override
    public ReviewDTO createForCourse(ReviewFormRequest request) {
        if (request.getRating() < 1 || request.getRating() > 5)
            throw new AppException(ErrorCode.RATE_OF_COMMENT);

        Review newReview = modelMapper.map(request, Review.class);
        newReview.setStudent(userService.getMyinfo());
        newReview.setId(null);
        newReview = reviewRepository.save(newReview);

        return modelMapper.map(newReview, ReviewDTO.class);
    }
}
