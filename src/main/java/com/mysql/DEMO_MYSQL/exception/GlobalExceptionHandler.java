package com.mysql.DEMO_MYSQL.exception;

import com.mysql.DEMO_MYSQL.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ===== 1. XỬ LÝ APPEXCEPTION (CUSTOM) =====
    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiResponse<Void>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.warn("AppException occurred: {}", errorCode.getMessage());

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(response);
    }

    // ===== 2. XỬ LÝ ACCESS DENIED =====
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse<Void>> handlingAuthorizationDeniedException(AccessDeniedException exception) {
        log.warn("AccessDeniedException: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(response);
    }

    // ===== 3. XỬ LÝ VALIDATION (MethodArgumentNotValidException) =====
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

        log.warn("Validation failed: {} - {}", enumKey, exception.getMessage());

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    // ===== 4. XỬ LÝ UNCATEGORIZED =====
    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<Void>> handlingException(Exception exception) {
        log.error("Unhandled exception occurred: ", exception);

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        response.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getHttpStatus()).body(response);
    }

    // ===== 5. XỬ LÝ AUTHENTICATION =====
    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ApiResponse<Void>> handlingAuthenticationException(AuthenticationException exception) {
        log.warn("Authentication failed: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.UN_AUTHENTICATED;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<ApiResponse<Void>> handlingBadCredentialsException(BadCredentialsException exception) {
        log.warn("Bad credentials: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.INVALID_CREDENTIALS;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    ResponseEntity<ApiResponse<Void>> handlingUsernameNotFoundException(UsernameNotFoundException exception) {
        log.warn("Username not found: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.USER_NOT_FOUND;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    // ===== 6. XỬ LÝ DATABASE =====
    @ExceptionHandler(DataAccessException.class)
    ResponseEntity<ApiResponse<Void>> handlingDataAccessException(DataAccessException exception) {
        log.error("Database error: ", exception);
        ErrorCode errorCode = ErrorCode.DATABASE_ERROR;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(SQLException.class)
    ResponseEntity<ApiResponse<Void>> handlingSQLException(SQLException exception) {
        log.error("SQL error: ", exception);
        ErrorCode errorCode = ErrorCode.DATABASE_ERROR;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage("Database error: " + exception.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    ResponseEntity<ApiResponse<Void>> handlingSQLIntegrityConstraintViolationException(
            SQLIntegrityConstraintViolationException exception) {
        log.warn("Data integrity violation: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.DATA_INTEGRITY_VIOLATION;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ApiResponse<Void>> handlingDataIntegrityViolationException(
            DataIntegrityViolationException exception) {
        log.warn("Data integrity violation: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.DATA_INTEGRITY_VIOLATION;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    ResponseEntity<ApiResponse<Void>> handlingDuplicateKeyException(DuplicateKeyException exception) {
        log.warn("Duplicate key error: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.DUPLICATE_ENTRY;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    // ===== 7. XỬ LÝ VALIDATION (ConstraintViolationException) =====
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiResponse<Void>> handlingConstraintViolationException(ConstraintViolationException exception) {
        log.warn("Constraint violation: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.CONSTRAINT_VIOLATION;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    // ===== 8. XỬ LÝ HTTP REQUEST =====
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<ApiResponse<Void>> handlingHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        log.warn("Method not supported: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    ResponseEntity<ApiResponse<Void>> handlingHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException exception) {
        log.warn("Media type not supported: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.MEDIA_TYPE_UNSUPPORTED;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    ResponseEntity<ApiResponse<Void>> handlingNoHandlerFoundException(NoHandlerFoundException exception) {
        log.warn("No handler found: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage("Endpoint not found: " + exception.getRequestURL());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    // ===== 9. XỬ LÝ MISSING PARAMETERS =====
    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<ApiResponse<Void>> handlingMissingServletRequestParameterException(
            MissingServletRequestParameterException exception) {
        log.warn("Missing request parameter: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.FIELD_REQUIRED;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage("Required parameter missing: " + exception.getParameterName());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    ResponseEntity<ApiResponse<Void>> handlingMissingRequestHeaderException(MissingRequestHeaderException exception) {
        log.warn("Missing request header: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.FIELD_REQUIRED;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage("Required header missing: " + exception.getHeaderName());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    ResponseEntity<ApiResponse<Void>> handlingMissingPathVariableException(MissingPathVariableException exception) {
        log.warn("Missing path variable: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.FIELD_REQUIRED;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage("Required path variable missing: " + exception.getVariableName());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    // ===== 10. XỬ LÝ TYPE MISMATCH =====
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ApiResponse<Void>> handlingMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception) {
        log.warn("Type mismatch: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.FIELD_INVALID;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage("Invalid value for parameter: " + exception.getName());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    // ===== 11. XỬ LÝ HTTP MESSAGE =====
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse<Void>> handlingHttpMessageNotReadableException(
            HttpMessageNotReadableException exception) {
        log.warn("Message not readable: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage("Invalid request body format");

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    // ===== 12. XỬ LÝ FILE UPLOAD =====
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    ResponseEntity<ApiResponse<Void>> handlingMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        log.warn("File size exceeded: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.FILE_TOO_LARGE;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MultipartException.class)
    ResponseEntity<ApiResponse<Void>> handlingMultipartException(MultipartException exception) {
        log.warn("Multipart error: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.FILE_UPLOAD_FAILED;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    // ===== 13. XỬ LÝ IO EXCEPTION =====
    @ExceptionHandler(IOException.class)
    ResponseEntity<ApiResponse<Void>> handlingIOException(IOException exception) {
        log.error("IO error: ", exception);
        ErrorCode errorCode = ErrorCode.OPERATION_FAILED;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage("Input/Output error occurred");

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    // ===== 14. XỬ LÝ ILLEGAL ARGUMENT =====
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiResponse<Void>> handlingIllegalArgumentException(IllegalArgumentException exception) {
        log.warn("Illegal argument: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(exception.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    // ===== 15. XỬ LÝ ILLEGAL STATE =====
    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<ApiResponse<Void>> handlingIllegalStateException(IllegalStateException exception) {
        log.warn("Illegal state: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.INVALID_OPERATION;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(exception.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    // ===== 16. XỬ LÝ NULL POINTER (CHỈ TRONG DEV) =====
    @ExceptionHandler(NullPointerException.class)
    ResponseEntity<ApiResponse<Void>> handlingNullPointerException(NullPointerException exception) {
        log.error("Null pointer exception: ", exception);
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage("An unexpected error occurred (null reference)");

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

 
}