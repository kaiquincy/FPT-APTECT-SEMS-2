package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentDTO {

    Long id;

    Integer studentId;

    Integer courseId;

    Integer progress;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime enrollmentDate;
}
