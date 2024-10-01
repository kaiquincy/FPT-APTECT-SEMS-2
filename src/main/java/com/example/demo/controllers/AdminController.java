package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.request.RejectCourseRequest;
import com.example.demo.entity.Course;
import com.example.demo.entity.Users;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.CourseService;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(value = "/api/admin")
@CrossOrigin(origins = "*") //Cho phep front end su dung API
@Slf4j
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private CourseService courseService;



	@GetMapping("/getalluser")
	ApiResponse<List<Users>> getAllUsers() {
		ApiResponse<List<Users>> apiResponse = new ApiResponse<>();
		apiResponse.setResult(userService.getAllUsers());
		return apiResponse;
	}

	@GetMapping("/getallcourse")
	ApiResponse<List<Course>> getAllCourse() {
		ApiResponse<List<Course>> apiResponse = new ApiResponse<>();
		apiResponse.setResult(courseRepository.findAll());
		return apiResponse;
	}

	@PostMapping("/approvalcourse/{courseId}")
	ApiResponse<String> postMethodName(@PathVariable Integer courseId) {
		ApiResponse<String> apiResponse = new ApiResponse<>();
		courseService.acceptCourse(courseId);
		apiResponse.setResult("Accepted the course successfully!");
		return apiResponse;
	}

	@PostMapping(value = "/rejectcourse/{courseId}")
	ApiResponse<String> postMethodName(@PathVariable Integer courseId, @RequestBody RejectCourseRequest request) {
		ApiResponse<String> apiResponse = new ApiResponse<>();
		courseService.rejectCourse(courseId, request.getReason());
		apiResponse.setResult("Reject the course successfully!");
		return apiResponse;
	}

	@PostMapping("/approvallecture/{lectureId}")
	ApiResponse<String> approveLecture(@PathVariable Integer lectureId) {
		ApiResponse<String> apiResponse = new ApiResponse<>();
		apiResponse.setResult(courseService.acceptLecture(lectureId));
		return apiResponse;
	}
	
}