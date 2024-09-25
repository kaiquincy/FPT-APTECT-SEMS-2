package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    UNCATEGORIZE_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1002, "Email existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1003, "User not existed", HttpStatus.NOT_FOUND),
    COURSE_NOT_EXISTED(1004, "Course not existed", HttpStatus.NOT_FOUND),
    ROLE_NOT_EXISTED(1005,"Role not existed", HttpStatus.NOT_FOUND),
    LECTURE_NOT_EXISTED(1005,"Lecture not existed", HttpStatus.NOT_FOUND),
    ENROLLMENT_EMPTY(1006, "This user have no enrollment", HttpStatus.NOT_FOUND),
    ENROLLMENT_EXISTED(1007, "User already enrolled in this course", HttpStatus.BAD_REQUEST),
    CART_NOT_EXISTED(1008, "Cart not existed", HttpStatus.NOT_FOUND),
    

    USERNAME_INVALID(1102, "Username must be between 5 and 15 characters", HttpStatus.BAD_REQUEST),
    USERNAME_NULL(1103, "Username cannot be null", HttpStatus.BAD_REQUEST),
    TITLE_NULL(1104, "Title cannot be null", HttpStatus.BAD_REQUEST),
    DESC_NULL(1105, "Desc cannot be null", HttpStatus.BAD_REQUEST),
    PRICE_NULL(1106, "Price cannot be null", HttpStatus.BAD_REQUEST),
    TEACHERID_NULL(1107, "Teacherid cannot be null", HttpStatus.BAD_REQUEST),
    EMAIL_BLANK(1111, "Email cannot be blank", HttpStatus.BAD_REQUEST),
    FULLNAME_BLANK(1121,"Fullname cannot be blank", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1131,"Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    FILE_MISSING(1122,"Please select a file to upload!", HttpStatus.BAD_REQUEST),

    PERMISSION_COURSE_DENIED(1201, "You don't have enough permission to this course", HttpStatus.FORBIDDEN),

    INSUFFICIENT_FUNDS(1301, "Insufficient funds to complete the purchase", HttpStatus.BAD_REQUEST),

    UNAUTHENTICATED(2001, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(2002, "Access denied", HttpStatus.FORBIDDEN),

    RATE_OF_COMMENT(2100, "Rating must be between 1 and 5.", HttpStatus.BAD_REQUEST),

    NOTIFICATION_NOT_FOUND(2200, "Notification not found", HttpStatus.BAD_REQUEST),
    MARK_AS_READ_FAIL(2201, "mark as read fail", HttpStatus.BAD_REQUEST)

    ;


    private int code;
    private String message;
    private HttpStatusCode statusCode;

    private ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }
    
}
