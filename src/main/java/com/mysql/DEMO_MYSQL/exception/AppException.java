package com.mysql.DEMO_MYSQL.exception;

public class AppException extends RuntimeException {
  private final ErrorCode errorCode; // final + camelCase

  public AppException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
