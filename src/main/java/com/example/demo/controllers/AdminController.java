package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.Users;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/admin")
@CrossOrigin(origins = "http://localhost:3000") //Cho phep front end su dung API
@Slf4j
public class AdminController {
	@Autowired
	private UserService userService;



	@GetMapping("/getalluser")
	ApiResponse<List<Users>> getAllUsers() {
		ApiResponse<List<Users>> apiResponse = new ApiResponse<>();
		apiResponse.setResult(userService.getAllUsers());
		return apiResponse;
	}
}
