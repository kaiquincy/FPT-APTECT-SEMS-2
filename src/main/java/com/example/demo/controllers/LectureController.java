package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.Lecture;
import com.example.demo.service.CourseService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/lecture")
@CrossOrigin(origins = "http://localhost:3000") //Cho phep front end su dung API
@Slf4j
public class LectureController {
    @Autowired
    CourseService courseService;

    @PostMapping("/{courseId}")
    ApiResponse<Lecture> addLecture(@PathVariable Integer courseId, @RequestBody Lecture lecture){
      return ApiResponse.<Lecture>builder().result(courseService.addLecture(courseId, lecture)).build();
    }
    
    @PutMapping("/{lectureId}")
    ApiResponse<Lecture> updateLecture(@PathVariable Integer lectureId , @RequestBody Lecture lecture) {
        return ApiResponse.<Lecture>builder().result(courseService.updateLecture(lectureId, lecture)).build();
    }

    @DeleteMapping("{lectureId}")
    ApiResponse<String> deleteLecture(@PathVariable Integer lectureId , @RequestBody Lecture lecture) {
      courseService.deleteLecture(lectureId);  
      return ApiResponse.<String>builder().result("Delete lecture completed!").build();
    }
}
