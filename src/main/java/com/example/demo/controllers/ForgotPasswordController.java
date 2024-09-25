package com.example.demo.controllers;

import com.example.demo.dto.ApiResponse;
import com.example.demo.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@CrossOrigin(origins = "http://localhost:3000")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot")
    public ApiResponse<String> forgotPassword(@RequestBody String email) {
        String token = forgotPasswordService.createPasswordResetToken(email);
        return ApiResponse.<String>builder().result(token).build();
    }
}
