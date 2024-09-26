package com.example.demo.service.imps;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.dto.request.ReviewFormRequest;
import com.example.demo.entity.Course;
import com.example.demo.entity.Review;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.CourseRepository;
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
    CourseRepository courseRepository;
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
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new AppException(ErrorCode.COURSE_NOT_EXISTED)
        );

        Review newReview = modelMapper.map(request, Review.class);
        newReview.setStudent(userService.getMyinfo());
        newReview.setId(null);

        // set average rating of course
        Double averageRating;
        List<Review> reviews = reviewRepository.findByCourseId(request.getCourseId());
        if (ArrayUtil.isNotEmpty(reviews)) {
            double totalRating = reviews.stream()
                    .mapToDouble(Review::getRating)
                    .sum() + request.getRating();
            int ratingCount = reviews.size() + 1;
            averageRating = totalRating / ratingCount;
        } else {
            averageRating = Double.valueOf(request.getRating());
        }
        averageRating = Math.round(averageRating * 10.0) / 10.0;
        course.setRate(averageRating);
        courseRepository.save(course);
        newReview = reviewRepository.save(newReview);

        return modelMapper.map(newReview, ReviewDTO.class);
    }
}
