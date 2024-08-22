package com.example.demo.controllers;

import com.example.demo.dto.ApiResponse;
import com.example.demo.enums.FileType;
import com.example.demo.service.AwsService;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/s3bucketstorage")
@CrossOrigin(origins = "http://localhost:3000") //Cho phep front end su dung API
public class AwsController {

    @Autowired
    private AwsService service;

    // Endpoint to list files in a bucket
    @GetMapping("/{bucketName}")
    public ResponseEntity<?> listFiles(
            @PathVariable("bucketName") String bucketName
    ) {
        val body = service.listFiles(bucketName);
        return ResponseEntity.ok(body);
    }

    // Endpoint to upload a file to a bucket
    @SuppressWarnings("null")
    @PostMapping("/{bucketName}/upload")
    @SneakyThrows(IOException.class)
    ApiResponse<String> uploadFile(
            @PathVariable("bucketName") String bucketName,
            @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            return ApiResponse.<String>builder().result("File is empty").build();
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String contentType = file.getContentType();
        long fileSize = file.getSize();
        InputStream inputStream = file.getInputStream();

        service.uploadFile(bucketName, fileName, fileSize, contentType, inputStream);

        return ApiResponse.<String>builder().result("File uploaded successfully").build();
    }

    // Endpoint to download a file from a bucket
    @SneakyThrows
    @GetMapping("/{bucketName}/download/{fileName}")
    public ResponseEntity<?> downloadFile(
            @PathVariable("bucketName") String bucketName,
            @PathVariable("fileName") String fileName
    ) {
        val body = service.downloadFile(bucketName, fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(FileType.fromFilename(fileName))
                .body(body.toByteArray());
    }

    // Endpoint to delete a file from a bucket
    @DeleteMapping("/{bucketName}/{fileName}")
    public ResponseEntity<?> deleteFile(
            @PathVariable("bucketName") String bucketName,
            @PathVariable("fileName") String fileName
    ) {
        service.deleteFile(bucketName, fileName);
        return ResponseEntity.ok().build();
    }
}
