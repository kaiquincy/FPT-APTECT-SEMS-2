package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.Course;
import com.example.demo.service.CourseService;
import com.example.demo.service.EmailService;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/course")
@CrossOrigin(origins = "*") // Позволяет фронтенду использовать API
@Slf4j
public class CourseController {

    @Autowired
    CourseService courseService;

    @Autowired
    EmailService emailService; // Внедряем EmailService

    @GetMapping
    ApiResponse<List<Course>> getAllCourse() {
        ApiResponse<List<Course>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(courseService.getAllCourse());
        return apiResponse;
    }

    @GetMapping("/{id}")
    ApiResponse<Course> getCourseById(@PathVariable Integer id) {
        ApiResponse<Course> apiResponse = new ApiResponse<>();
        apiResponse.setResult(courseService.getCourseById(id));
        return apiResponse;
    }

    @PostMapping
    ApiResponse<Course> createCourse(@Valid @RequestParam("title") String title,
                                            @RequestParam("description") String description,
                                            @RequestParam("price") Double price,
                                            @RequestParam("img") MultipartFile image)
    {
        ApiResponse<Course> apiResponse = new ApiResponse<>();
        apiResponse.setResult(courseService.createCourse(title, description, price, image));
        return apiResponse;
    }

    @PutMapping("/{id}")
    ApiResponse<String> updateCourse(@PathVariable Integer id, @Valid @RequestBody Course entity) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(courseService.updateCourse(id, entity));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteCourseById(@PathVariable Integer id) {
        courseService.deleteCourse(id);
        return ApiResponse.<String>builder().result("Course has been deleted").build();
    }

    @GetMapping("/mynewestcourseid")
    ApiResponse<Integer> getMyNewestCourseId() {
        ApiResponse<Integer> apiResponse = new ApiResponse<>();
        apiResponse.setResult(courseService.getMyNewestCourseId());
        return apiResponse;
    }

    @GetMapping("/my-courses")
    ApiResponse<List<Course>> getallCoursesById() {
        ApiResponse<List<Course>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(courseService.getallCoursesById());
        return apiResponse;
    }

    // new method
    @PostMapping("/confirm-register")
    ApiResponse<String> confirmRegisterCourse(@RequestBody ConfirmRegistrationRequest request) {
        String msg = "You have successfully registered for the course! Click this link to navigate to your course : " + request.linkCourse;
        String email = request.getEmail(); // method nay nha

        emailService.confirmRegisterCourse(msg, email);

        String responseMessage ="Hello! Confirmation email has been sent to " + email;

        return ApiResponse.<String>builder().result(responseMessage).build();
    }

    // request dki moi
    @Getter
    @Setter
    static class ConfirmRegistrationRequest {
        private String email;
        private String linkCourse;
    }

    
}