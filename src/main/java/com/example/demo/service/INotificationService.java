package com.example.demo.service;

import com.example.demo.dto.NotificationDTO;
import com.example.demo.dto.request.NotificationFormRequest;

import java.util.List;

public interface INotificationService {

    List<NotificationDTO> getMyNotifications();

    NotificationDTO create(NotificationFormRequest request);

    NotificationDTO markAsRead(Long notificationId);

}
