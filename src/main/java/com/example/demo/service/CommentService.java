package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getCommentsByLectureId(Integer lectureId) {
        return commentRepository.findByLectureId(lectureId);
    }

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }
}