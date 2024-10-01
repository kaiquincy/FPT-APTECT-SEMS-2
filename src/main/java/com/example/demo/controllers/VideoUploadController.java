package com.example.demo.controllers;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ApiResponse;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@CrossOrigin(origins = "http://localhost:3000") //Cho phep front end su dung API
@Slf4j
public class VideoUploadController {

    @PostMapping("/api/upload-video")
    public ApiResponse<String> uploadVideo(
            @RequestHeader("Authorization") String authToken,
            @RequestParam("file") MultipartFile file) {
        
        if (authToken == null || authToken.isEmpty()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_MISSING);
        }

        try {
            // Lấy thời gian hiện tại và định dạng nó
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
            String formattedTime = currentTime.format(formatter);

            // Lấy phần mở rộng của file
            String originalFilename = file.getOriginalFilename();
            @SuppressWarnings("null")
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // Tạo tên file mới với thời gian hiện tại
            String newFileName = "video_" + formattedTime + extension;

            String projectRootPath = System.getProperty("user.dir");
            File uploadDir = new File(projectRootPath,"pending/videos");

            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // Tạo tất cả các thư mục con nếu cần
            }

            // Lưu file vào thư mục trên server
            File videoFile = new File(uploadDir,newFileName);
            file.transferTo(videoFile);

            // return new ResponseEntity<>("Successfully uploaded - " + newFileName, HttpStatus.OK);
            return ApiResponse.<String>builder().result(newFileName).build();

        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.UNCATEGORIZE_EXCEPTION);
        }
    }
}
