package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.Course;
import com.example.demo.service.CourseService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping(value = "/api/course")
@CrossOrigin(origins = "http://localhost:3000") //Cho phep front end su dung API
@Slf4j
public class CourseController {
    @Autowired
    CourseService courseService;

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
    ApiResponse<Course> createCourse(@Valid @RequestBody Course entity) {
		ApiResponse<Course> apiResponse = new ApiResponse<>();
		apiResponse.setResult(courseService.createCourse(entity));
		return apiResponse;
    }
    
    @PutMapping("/{id}")
    ApiResponse<Course> updateCourse(@PathVariable Integer id, @Valid @RequestBody Course entity) {
		ApiResponse<Course> apiResponse = new ApiResponse<>();
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
	}
