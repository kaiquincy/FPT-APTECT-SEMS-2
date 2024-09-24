package com.example.demo.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserCreationRequest;
import com.example.demo.entity.Users;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;






@RestController
@RequestMapping(value = "/api/user")
@CrossOrigin(origins = "http://localhost:3000") //Cho phep front end su dung API
@Slf4j
public class UserController {
	@Autowired
	private UserService userService;

	@Operation(summary = "Get user by id ok!",
            description = "Get all, get metaData in response, page start from 0, default page =0, size = 10"
    )
    @GetMapping("/{id}") 
    ApiResponse<Users> getUserById(@PathVariable Long id) {
		ApiResponse<Users> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUserById(id));
		return apiResponse;
    }	

	@GetMapping("/myinfo")
	public ApiResponse<Users> getMyInfo() {
		ApiResponse<Users> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getMyinfo());
		return apiResponse;
	}
	

	@PostMapping
	ApiResponse<Users> addUser(@Valid @RequestBody UserCreationRequest user) {
		ApiResponse<Users> apiResponse = new ApiResponse<>();
		apiResponse.setResult(userService.saveUser(user));
		return apiResponse;
	}
	
	@PutMapping("/{id}")
	ApiResponse<Users> updateUserById(@PathVariable Long id, @Valid @RequestBody Users user) {		
		ApiResponse<Users> apiResponse = new ApiResponse<>();
		apiResponse.setResult(userService.updateUser(id, user));
		return apiResponse;
	}
	
	@DeleteMapping("/{id}")
	ApiResponse<String> deleteUserById(@PathVariable Long id) {
		userService.deleteUser(id);
		return ApiResponse.<String>builder().result("User has been deleted").build();
	}
	

	@PostMapping("/{id}/assign-role")
	ApiResponse<String> postMethodName(@PathVariable Long id, @RequestBody String role) {
		return ApiResponse.<String>builder().result(userService.assignRole(id, role)).build();
	}
	

}