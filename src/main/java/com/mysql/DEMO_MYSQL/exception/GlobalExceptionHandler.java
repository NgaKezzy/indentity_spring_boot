package com.mysql.DEMO_MYSQL.exception;

import com.mysql.DEMO_MYSQL.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse<Void>> handlingRuntimeException(RuntimeException exception){
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Error: " + exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiResponse);
    }

    // Optional: Handle other exception types
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Void>> handlingGenericException(Exception exception){
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(false);
        apiResponse.setMessage("Internal server error: " + exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiResponse);
    }
}