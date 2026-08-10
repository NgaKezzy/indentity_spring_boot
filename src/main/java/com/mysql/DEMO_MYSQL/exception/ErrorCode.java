package com.mysql.DEMO_MYSQL.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
  USER_EXISTED(1001, "User existed", HttpStatus.CONFLICT),
  USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
  INVALID_CREDENTIALS(1003, "Invalid username or password", HttpStatus.UNAUTHORIZED),
  USERNAME_INVALID(
      1004, "Username must be {min} characters and cannot be blank", HttpStatus.BAD_REQUEST),
  PASSWORD_INVALID(1005, "Password cannot be blank", HttpStatus.BAD_REQUEST),
  PASSWORD_TOO_WEAK(1006, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
  INVALID_REQUEST(1007, "Invalid request", HttpStatus.BAD_REQUEST),
  UN_AUTHENTICATED(1008, "Unauthenticated", HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED(1009, "You do not have permission", HttpStatus.FORBIDDEN),
  // ===== THÊM MỚI: USER & AUTHENTICATION =====
  EMAIL_EXISTED(1010, "Email already registered", HttpStatus.CONFLICT),
  PHONE_EXISTED(1011, "Phone number already registered", HttpStatus.CONFLICT),
  USER_DISABLED(1012, "User account is disabled", HttpStatus.FORBIDDEN),
  USER_LOCKED(1013, "User account is locked", HttpStatus.FORBIDDEN),
  USER_EXPIRED(1014, "User account has expired", HttpStatus.FORBIDDEN),
  INVALID_EMAIL(1015, "Invalid email format", HttpStatus.BAD_REQUEST),
  INVALID_PHONE(1016, "Invalid phone number format", HttpStatus.BAD_REQUEST),
  TOKEN_EXPIRED(1017, "Token has expired", HttpStatus.UNAUTHORIZED),
  TOKEN_INVALID(1018, "Invalid token", HttpStatus.UNAUTHORIZED),
  TOKEN_MISSING(1019, "Token is missing", HttpStatus.UNAUTHORIZED),
  REFRESH_TOKEN_INVALID(1020, "Invalid refresh token", HttpStatus.UNAUTHORIZED),
  REFRESH_TOKEN_EXPIRED(1021, "Refresh token has expired", HttpStatus.UNAUTHORIZED),
  WRONG_PASSWORD(1022, "Wrong password", HttpStatus.UNAUTHORIZED),
  PASSWORD_MISMATCH(1023, "Password confirmation does not match", HttpStatus.BAD_REQUEST),

  // ===== ROLE & PERMISSION =====
  ROLE_NOT_FOUND(1030, "Role not found", HttpStatus.NOT_FOUND),
  PERMISSION_NOT_FOUND(1031, "Permission not found", HttpStatus.NOT_FOUND),
  ROLE_EXISTED(1032, "Role already exists", HttpStatus.CONFLICT),
  PERMISSION_EXISTED(1033, "Permission already exists", HttpStatus.CONFLICT),
  INSUFFICIENT_PERMISSIONS(
      1034, "Insufficient permissions to perform this action", HttpStatus.FORBIDDEN),

  // ===== RESOURCE & DATA =====
  RESOURCE_NOT_FOUND(1040, "Resource not found", HttpStatus.NOT_FOUND),
  DUPLICATE_ENTRY(1041, "Duplicate entry", HttpStatus.CONFLICT),
  DATA_INTEGRITY_VIOLATION(1042, "Data integrity violation", HttpStatus.CONFLICT),
  CONSTRAINT_VIOLATION(1043, "Constraint violation", HttpStatus.BAD_REQUEST),

  // ===== VALIDATION =====
  FIELD_REQUIRED(1050, "This field is required", HttpStatus.BAD_REQUEST),
  FIELD_INVALID(1051, "Invalid field value", HttpStatus.BAD_REQUEST),
  FIELD_TOO_SHORT(1052, "Field value is too short", HttpStatus.BAD_REQUEST),
  FIELD_TOO_LONG(1053, "Field value is too long", HttpStatus.BAD_REQUEST),
  FIELD_MIN_VALUE(1054, "Value is below minimum allowed", HttpStatus.BAD_REQUEST),
  FIELD_MAX_VALUE(1055, "Value exceeds maximum allowed", HttpStatus.BAD_REQUEST),
  DATE_INVALID(1056, "Invalid date format", HttpStatus.BAD_REQUEST),
  DATE_FUTURE(1057, "Date cannot be in the future", HttpStatus.BAD_REQUEST),
  DATE_PAST(1058, "Date cannot be in the past", HttpStatus.BAD_REQUEST),
  DOB_UNDER_AGE(1059, "User must be at least {min} years old", HttpStatus.BAD_REQUEST),

  // ===== BUSINESS LOGIC =====
  OPERATION_NOT_ALLOWED(1060, "Operation not allowed", HttpStatus.FORBIDDEN),
  OPERATION_FAILED(1061, "Operation failed", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_OPERATION(1062, "Invalid operation", HttpStatus.BAD_REQUEST),
  TRANSACTION_FAILED(1063, "Transaction failed", HttpStatus.INTERNAL_SERVER_ERROR),
  CONCURRENT_MODIFICATION(1064, "Data was modified by another user", HttpStatus.CONFLICT),

  // ===== FILE & UPLOAD =====
  FILE_TOO_LARGE(1070, "File size exceeds maximum allowed", HttpStatus.BAD_REQUEST),
  FILE_EMPTY(1071, "File is empty", HttpStatus.BAD_REQUEST),
  FILE_INVALID_TYPE(1072, "Invalid file type", HttpStatus.BAD_REQUEST),
  FILE_UPLOAD_FAILED(1073, "File upload failed", HttpStatus.INTERNAL_SERVER_ERROR),
  FILE_NOT_FOUND(1074, "File not found", HttpStatus.NOT_FOUND),

  // ===== DATABASE =====
  DATABASE_ERROR(1080, "Database error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
  QUERY_FAILED(1081, "Query execution failed", HttpStatus.INTERNAL_SERVER_ERROR),
  CONNECTION_FAILED(1082, "Database connection failed", HttpStatus.INTERNAL_SERVER_ERROR),
  TIMEOUT_ERROR(1083, "Operation timed out", HttpStatus.REQUEST_TIMEOUT),

  // ===== EXTERNAL SERVICE =====
  EXTERNAL_SERVICE_ERROR(1090, "External service error", HttpStatus.BAD_GATEWAY),
  EXTERNAL_SERVICE_TIMEOUT(1091, "External service timeout", HttpStatus.GATEWAY_TIMEOUT),
  EXTERNAL_SERVICE_UNAVAILABLE(
      1092, "External service unavailable", HttpStatus.SERVICE_UNAVAILABLE),
  API_RATE_LIMIT_EXCEEDED(1093, "API rate limit exceeded", HttpStatus.TOO_MANY_REQUESTS),

  // ===== MAIL & NOTIFICATION =====
  EMAIL_SEND_FAILED(1100, "Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR),
  EMAIL_TEMPLATE_ERROR(1101, "Email template error", HttpStatus.INTERNAL_SERVER_ERROR),
  SMS_SEND_FAILED(1102, "Failed to send SMS", HttpStatus.INTERNAL_SERVER_ERROR),
  NOTIFICATION_FAILED(1103, "Failed to send notification", HttpStatus.INTERNAL_SERVER_ERROR),

  // ===== CACHE =====
  CACHE_ERROR(1110, "Cache operation failed", HttpStatus.INTERNAL_SERVER_ERROR),
  CACHE_MISS(1111, "Cache miss", HttpStatus.NOT_FOUND),

  // ===== SECURITY =====
  CSRF_INVALID(1120, "Invalid CSRF token", HttpStatus.FORBIDDEN),
  SESSION_EXPIRED(1121, "Session expired", HttpStatus.UNAUTHORIZED),
  IP_BLOCKED(1122, "IP address is blocked", HttpStatus.FORBIDDEN),
  TOO_MANY_ATTEMPTS(1123, "Too many failed attempts", HttpStatus.TOO_MANY_REQUESTS),

  // ===== PAGINATION & SORTING =====
  INVALID_PAGE(1130, "Invalid page number", HttpStatus.BAD_REQUEST),
  INVALID_PAGE_SIZE(1131, "Invalid page size", HttpStatus.BAD_REQUEST),
  INVALID_SORT(1132, "Invalid sort field", HttpStatus.BAD_REQUEST),

  // ===== GENERAL =====
  BAD_REQUEST(1140, "Bad request", HttpStatus.BAD_REQUEST),
  METHOD_NOT_ALLOWED(1141, "Method not allowed", HttpStatus.METHOD_NOT_ALLOWED),
  MEDIA_TYPE_UNSUPPORTED(1142, "Unsupported media type", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
  SERVICE_UNAVAILABLE(1143, "Service temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE),

  // ===== PAYMENT (nếu có) =====
  PAYMENT_FAILED(1150, "Payment failed", HttpStatus.BAD_REQUEST),
  PAYMENT_NOT_FOUND(1151, "Payment not found", HttpStatus.NOT_FOUND),
  PAYMENT_ALREADY_PROCESSED(1152, "Payment already processed", HttpStatus.CONFLICT),
  INSUFFICIENT_BALANCE(1153, "Insufficient balance", HttpStatus.BAD_REQUEST),
  INVALID_CARD(1154, "Invalid card information", HttpStatus.BAD_REQUEST),

  // ===== ORDER (nếu có) =====
  ORDER_NOT_FOUND(1160, "Order not found", HttpStatus.NOT_FOUND),
  ORDER_CANCELLED(1161, "Order has been cancelled", HttpStatus.BAD_REQUEST),
  ORDER_COMPLETED(1162, "Order already completed", HttpStatus.BAD_REQUEST),
  ORDER_PENDING(1163, "Order is pending", HttpStatus.BAD_REQUEST),
  OUT_OF_STOCK(1164, "Product is out of stock", HttpStatus.BAD_REQUEST),

  // ===== PRODUCT (nếu có) =====
  PRODUCT_NOT_FOUND(1170, "Product not found", HttpStatus.NOT_FOUND),
  PRODUCT_EXISTED(1171, "Product already exists", HttpStatus.CONFLICT),
  CATEGORY_NOT_FOUND(1172, "Category not found", HttpStatus.NOT_FOUND),
  CATEGORY_EXISTED(1173, "Category already exists", HttpStatus.CONFLICT),

  // ===== OTP/VERIFICATION =====
  OTP_INVALID(1180, "Invalid OTP code", HttpStatus.BAD_REQUEST),
  OTP_EXPIRED(1181, "OTP code has expired", HttpStatus.BAD_REQUEST),
  OTP_TOO_MANY_ATTEMPTS(1182, "Too many OTP attempts", HttpStatus.TOO_MANY_REQUESTS),
  VERIFICATION_FAILED(1183, "Verification failed", HttpStatus.BAD_REQUEST),

  // ===== IMAGE =====
  IMAGE_INVALID(1190, "Invalid image format", HttpStatus.BAD_REQUEST),
  IMAGE_TOO_LARGE(1191, "Image size exceeds maximum allowed", HttpStatus.BAD_REQUEST),
  IMAGE_PROCESSING_FAILED(1192, "Image processing failed", HttpStatus.INTERNAL_SERVER_ERROR);

  private final int code;
  private final String message;
  private final HttpStatus httpStatus;

  ErrorCode(int code, String message, HttpStatus httpStatus) {
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
