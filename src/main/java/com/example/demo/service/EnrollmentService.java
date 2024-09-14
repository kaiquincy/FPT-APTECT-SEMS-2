package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Enrollment;
import com.example.demo.entity.Transaction;
import com.example.demo.enums.TransactionType;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;


@Service
public class EnrollmentService {
    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    UserService userService;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    TransactionService transactionService;

    public Enrollment addEnrollment(Integer courseId){
        if(!courseRepository.existsById(courseId)){
            throw new AppException(ErrorCode.COURSE_NOT_EXISTED);
        }

        if(enrollmentRepository.existsByStudentIdAndCourseId(userService.getMyinfo().getId(), courseId)){
            throw new AppException(ErrorCode.ENROLLMENT_EXISTED);
        }

        // if(userService.getMyinfo().getBalance() < courseRepository.findById(courseId).get().getPrice()){
        //     throw new AppException(ErrorCode.INSUFFICIENT_FUNDS);
        // }

        Transaction transaction = new Transaction();
        transaction.setAmount(courseRepository.findById(courseId).get().getPrice());
        transaction.setTransactionType(TransactionType.PURCHASE);
        transactionService.createTransaction(transaction);


        Enrollment enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentId(userService.getMyinfo().getId());
        enrollment.setProgress(0);

        return(enrollmentRepository.save(enrollment));
    }

    public List<Enrollment> getMyEnrollments(){
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(userService.getMyinfo().getId());
        
        if (enrollments == null || enrollments.isEmpty()) {
            throw new AppException(ErrorCode.ENROLLMENT_EMPTY);
        }
        
        return enrollments;
    }
}
