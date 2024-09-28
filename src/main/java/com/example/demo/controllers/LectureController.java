package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Lecture;
import com.example.demo.service.CommentService;
import com.example.demo.service.CourseService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping(value = "/api/lecture")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class LectureController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/{courseId}")
    public ApiResponse<Lecture> addLecture(@PathVariable Integer courseId, @RequestBody Lecture lecture) {
        return ApiResponse.<Lecture>builder().result(courseService.addLecture(courseId, lecture)).build();
    }

    @PutMapping("/{lectureId}")
    public ApiResponse<Lecture> updateLecture(@PathVariable Integer lectureId, @RequestBody Lecture lecture) {
        return ApiResponse.<Lecture>builder().result(courseService.updateLecture(lectureId, lecture)).build();
    }

    @DeleteMapping("{lectureId}")
    public ApiResponse<String> deleteLecture(@PathVariable Integer lectureId) {
        courseService.deleteLecture(lectureId);
        return ApiResponse.<String>builder().result("Delete lecture completed!").build();
    }

    @GetMapping("/comment/{lectureId}")
    public ApiResponse<List<Comment>> getComments(@PathVariable Integer lectureId) {
        List<Comment> comments = commentService.getCommentsByLectureId(lectureId);
        return ApiResponse.<List<Comment>>builder().result(comments).build();
    }

    @PostMapping("/comment/{lectureId}")
    public ApiResponse<Comment> addComment(@PathVariable Integer lectureId, @RequestBody Comment comment) {
        comment.setLectureId(lectureId); // Устанавливаем ID лекции для комментария
        Comment savedComment = commentService.addComment(comment);
        return ApiResponse.<Comment>builder().result(savedComment).build();
    }
}