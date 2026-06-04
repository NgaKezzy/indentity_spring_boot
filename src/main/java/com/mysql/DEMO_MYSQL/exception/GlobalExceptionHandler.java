package com.mysql.DEMO_MYSQL.exception;

import com.mysql.DEMO_MYSQL.dto.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiResponse<Void>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        ApiResponse<Void> response = new ApiResponse();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus())  // Quan trọng: dùng status từ ErrorCode
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> handlingValidationException(MethodArgumentNotValidException exception) {

        String enumKey = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        ErrorCode errorCode;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            log.warn("Unknown validation message: {}", enumKey);
            errorCode = ErrorCode.INVALID_REQUEST;
        }

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<Void>> handlingException(Exception exception) {
        log.error("Unhandled exception: ", exception);

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        response.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getHttpStatus()).body(response);
    }
}