package com.example.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.request.VideoResultRequest;
import com.example.demo.entity.Lecture;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.SettingRepository;
import com.example.demo.service.CourseService;
import com.example.demo.service.VideoUploadService;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "http://localhost:3000") //Cho phep front end su dung API
@Slf4j
public class VideoUploadController {

    @Autowired
    SettingRepository settingRepository;
    @Autowired
    CourseService courseService;
    @Autowired
    VideoUploadService videoUploadService;

    @PostMapping("/api/upload-video")
    public ApiResponse<String> uploadVideo(
            @RequestHeader("Authorization") String authToken,
            @RequestParam("file") MultipartFile file,
            @RequestParam("lecture_id") String lecture_id,
            @RequestParam("title") String title) {
        
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
            File uploadDir = new File(projectRootPath,"pending/video");

            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // Tạo tất cả các thư mục con nếu cần
            }

            // Lưu file vào thư mục trên server
            File videoFile = new File(uploadDir,newFileName);
            file.transferTo(videoFile);

            // cho tên video vào database
            Lecture lecture = courseService.getLectureById(Integer.parseInt(lecture_id));
            lecture.setVideo(newFileName);
            courseService.updateLecture(Integer.parseInt(lecture_id), lecture);


            // Kiểm tra xem có đang set auto_accept là "true" ko ?
            String auto_accept = settingRepository.findBySettingKey("auto_accept").get().getValue();
            System.out.println(auto_accept);
            if ("true".equals(auto_accept)){
                //Gọi tới api valid video python
                VideoUploadService videoUploader = new VideoUploadService(new RestTemplate());
                String result = videoUploader.uploadVideoToValidationAPI(title, lecture_id, videoFile);

                return ApiResponse.<String>builder().result(result).build();
            }
            
            return ApiResponse.<String>builder().result(newFileName).build();

        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.UNCATEGORIZE_EXCEPTION);
        }
    }


    @PostMapping("/api/hook/video-result")
    public void getVideoResult(@RequestBody VideoResultRequest request) {
        if (request.getValid() == "true"){
            courseService.acceptLecture(request.getLecture_id());
        } else {
            courseService.rejectLecture(request.getLecture_id(), request.getReason());
            courseService.rejectCourse(courseService.getLectureById(request.getLecture_id()).getCourseId(), request.getReason());
        }

        // Nếu tất cả lecture đã APPROVED thì duyệt nó
        Integer courseId = courseService.getLectureById(request.getLecture_id()).getCourseId();
        List<Lecture> lectures = courseService.getAllLectureByCourseId(courseId);

        System.out.println(lectures.size());

        for (Lecture lecture : lectures){
            System.out.println(lecture.getState());
            if ("REJECTED".equals(lecture.getState()) || "PENDING".equals(lecture.getState())) {
                return;
            }
        }


        courseService.acceptCourse(courseId);
    }
    
}





