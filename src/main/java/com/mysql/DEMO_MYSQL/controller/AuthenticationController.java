package com.mysql.DEMO_MYSQL.controller;

import com.mysql.DEMO_MYSQL.dto.request.authent.AuthenticationRequest;
import com.mysql.DEMO_MYSQL.dto.request.authent.IntroSpectTokenRequest;
import com.mysql.DEMO_MYSQL.dto.request.authent.LogoutRequest;
import com.mysql.DEMO_MYSQL.dto.request.authent.RefreshRequest;
import com.mysql.DEMO_MYSQL.dto.response.ApiResponse;
import com.mysql.DEMO_MYSQL.dto.response.authent.IntroSpectTokenResponse;
import com.mysql.DEMO_MYSQL.dto.response.authent.RefreshTokenResponse;
import com.mysql.DEMO_MYSQL.dto.response.user.UserResponse;
import com.mysql.DEMO_MYSQL.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication", description = "Đăng nhập và quản lý JWT")
@SecurityRequirements
public class AuthenticationController {
    AuthenticationService authenticationService;

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    @Operation(summary = "Đăng nhập", description = "Xác thực tài khoản và trả về access token cùng refresh token")
    ApiResponse<UserResponse> authenticate(@RequestBody AuthenticationRequest request) {
        UserResponse userResponse = authenticationService.authenticate(request);
        return ApiResponse.<UserResponse>builder().data(userResponse).success(true).build();
    }

    @PostMapping("/introspect")
    @Operation(summary = "Kiểm tra token", description = "Kiểm tra JWT còn hợp lệ hay không")
    ApiResponse<IntroSpectTokenResponse> introSpectToken(@RequestBody IntroSpectTokenRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introSpectToken(request);

        return ApiResponse.<IntroSpectTokenResponse>builder().data(result).success(true).build();
    }

    @PostMapping("/logout")
    @Operation(summary = "Đăng xuất", description = "Vô hiệu hóa token hiện tại")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().success(true).build();
    }

    @PostMapping("/refresh")
    @Operation(summary = "Làm mới token", description = "Cấp access token mới từ refresh token")
    ApiResponse<RefreshTokenResponse> refreshToken(@RequestBody @Valid RefreshRequest request)
            throws ParseException, JOSEException {
        RefreshTokenResponse refreshTokenResponse = authenticationService.refreshToken(request);
        return ApiResponse.<RefreshTokenResponse>builder().data(refreshTokenResponse).success(true).build();
    }
}
