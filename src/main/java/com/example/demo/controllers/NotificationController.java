package com.example.demo.controllers;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.NotificationDTO;
import com.example.demo.dto.request.NotificationFormRequest;
import com.example.demo.service.INotificationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/notifications")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class NotificationController {

    INotificationService notificationService;

    @GetMapping("/me")
    ApiResponse<?> getNotifications() {
        ApiResponse<List<NotificationDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(notificationService.getMyNotifications());
        return apiResponse;
    }


    @PostMapping("")
    ApiResponse<?> create(@Valid @RequestBody NotificationFormRequest request) {
        ApiResponse<NotificationDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(notificationService.create(request));
        return apiResponse;
    }

    @PostMapping("/{notification-id}")
    ApiResponse<?> create(@PathVariable(name = "notification-id") Long notificationId) {
        ApiResponse<NotificationDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(notificationService.markAsRead(notificationId));
        return apiResponse;
    }
}
