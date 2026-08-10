package com.mysql.DEMO_MYSQL.configuration;

import com.mysql.DEMO_MYSQL.dto.response.ApiResponse;
import com.mysql.DEMO_MYSQL.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;

public class JwtAuthenticatedEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    ErrorCode errorCode = ErrorCode.UN_AUTHENTICATED;
    response.setStatus(errorCode.getHttpStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    ApiResponse<?> apiResponse =
        ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build();
    ObjectMapper objectMapper = new ObjectMapper();
    response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    response.flushBuffer();
  }
}
