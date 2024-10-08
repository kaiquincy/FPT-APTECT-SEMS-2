package com.example.demo.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

@Service
public class VideoUploadService {

    private final RestTemplate restTemplate;

    public VideoUploadService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String uploadVideoToValidationAPI(String title, String lectureId, File videoFile) throws IOException {
        // URL của API bạn cần gửi POST request
        String url = "https://positive-equally-martin.ngrok-free.app/request/valid-video";

        // Tạo header cho yêu cầu
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Tạo body cho form-data
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("title", title);
        body.add("lecture_id", lectureId);
        body.add("video", new FileSystemResource(videoFile));

        // Tạo yêu cầu với headers và body
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Gửi yêu cầu POST tới API
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // Kiểm tra phản hồi từ API
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // Trả về kết quả từ API
        } else {
            throw new RuntimeException("Failed to upload video. Status Code: " + response.getStatusCode());
        }
    }
}