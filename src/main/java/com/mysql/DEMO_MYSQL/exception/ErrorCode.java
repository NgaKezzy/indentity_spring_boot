package com.mysql.DEMO_MYSQL.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_EXISTED(1001, "User existed", HttpStatus.CONFLICT),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS(1003, "Invalid username or password", HttpStatus.UNAUTHORIZED),
    USERNAME_INVALID(1004, "Username must be 3-20 characters and cannot be blank", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1005, "Password cannot be blank", HttpStatus.BAD_REQUEST),
    PASSWORD_TOO_WEAK(1006, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(1007, "Invalid request", HttpStatus.BAD_REQUEST),
    UN_AUTHENTICATED(1008, "Unauthenticated", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }


}