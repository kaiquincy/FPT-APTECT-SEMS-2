package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.dto.EnrollmentDTO;
import com.example.demo.entity.Course;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Enrollment;
import com.example.demo.entity.Transaction;
import com.example.demo.enums.TransactionType;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import org.springframework.transaction.annotation.Transactional;


@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class EnrollmentService {

    EnrollmentRepository enrollmentRepository;
    UserService userService;
    CourseRepository courseRepository;
    TransactionService transactionService;
    ModelMapper modelMapper;

    @Transactional
    public EnrollmentDTO addEnrollment(Integer courseId){
        Course course = findCourseById(courseId);

        if(enrollmentRepository.existsByStudentIdAndCourseId(userService.getMyinfo().getId(), courseId)){
            throw new AppException(ErrorCode.ENROLLMENT_EXISTED);
        }

        if(userService.getMyinfo().getBalance() < course.getPrice()){
            throw new AppException(ErrorCode.INSUFFICIENT_FUNDS);
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(course.getPrice());
        transaction.setTransactionType(TransactionType.PURCHASE);
        transactionService.createTransaction(transaction);


        Enrollment enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentId(userService.getMyinfo().getId());
        enrollment.setProgress(0);
        enrollment.setEnrollmentDate(LocalDateTime.now());

        enrollment = enrollmentRepository.save(enrollment);

        return modelMapper.map(enrollment, EnrollmentDTO.class);
    }

    @Transactional
    public EnrollmentDTO updateEnrollment(Integer courseId){
        findCourseById(courseId);

        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(userService.getMyinfo().getId(), courseId);
        if(enrollment == null ){
            throw new AppException(ErrorCode.ENROLLMENT_EXISTED);
        }

        enrollment.setProgress(enrollment.getProgress() + 1);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment = enrollmentRepository.save(enrollment);

        return modelMapper.map(enrollment, EnrollmentDTO.class);
    }

    public List<EnrollmentDTO> getMyEnrollments(){
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(userService.getMyinfo().getId());
        
        if (enrollments == null || enrollments.isEmpty()) {
            throw new AppException(ErrorCode.ENROLLMENT_EMPTY);
        }
        
        return enrollments.stream().map(
                enrollment -> modelMapper.map(enrollment, EnrollmentDTO.class)
        ).toList();
    }

    private Course findCourseById(Integer courseId) {
        return courseRepository.findById(courseId).orElseThrow(
                () -> new AppException(ErrorCode.COURSE_NOT_EXISTED)
        );
    }
}
