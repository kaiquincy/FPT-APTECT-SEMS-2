package com.example.demo.service.imps;

import com.example.demo.dto.NotificationDTO;
import com.example.demo.dto.request.NotificationFormRequest;
import com.example.demo.entity.Notification;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.INotificationService;
import com.example.demo.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NotificationService implements INotificationService {

    NotificationRepository notificationRepository;
    UserService userService;
    ModelMapper modelMapper;
    UserRepository userRepository;

    @Override
    public List<NotificationDTO> getMyNotifications() {

        Integer userId = userService.getMyinfo().getId();

        List<Notification> notificationList = notificationRepository.findByStudentId(userId);

        return notificationList.stream().map(
                notification -> modelMapper.map(notification, NotificationDTO.class)
                ).toList();
    }

    @Override
    public NotificationDTO create(NotificationFormRequest request) {
        if (!userRepository.existsById(request.getStudentId()))
            throw new AppException(ErrorCode.USER_NOT_EXISTED);

        Notification notification = modelMapper.map(request, Notification.class);
        notification = notificationRepository.save(notification);
        return modelMapper.map(notification, NotificationDTO.class);
    }

    @Override
    public NotificationDTO markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND)
        );
        Integer userId = userService.getMyinfo().getId();

        if (!userId.equals(notification.getStudentId()))
            throw new AppException(ErrorCode.MARK_AS_READ_FAIL);

        notification.setRead(true);
        notificationRepository.save(notification);
        return modelMapper.map(notification, NotificationDTO.class);
    }
}
