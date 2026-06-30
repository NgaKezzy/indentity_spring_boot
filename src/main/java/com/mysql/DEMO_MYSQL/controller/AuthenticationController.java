package com.mysql.DEMO_MYSQL.controller;

import com.mysql.DEMO_MYSQL.dto.request.AuthenticationRequest;
import com.mysql.DEMO_MYSQL.dto.request.IntroSpectTokenRequest;
import com.mysql.DEMO_MYSQL.dto.response.ApiResponse;
import com.mysql.DEMO_MYSQL.dto.response.IntroSpectTokenResponse;
import com.mysql.DEMO_MYSQL.dto.response.UserResponse;
import com.mysql.DEMO_MYSQL.exception.AppException;
import com.mysql.DEMO_MYSQL.exception.ErrorCode;
import com.mysql.DEMO_MYSQL.mapper.UserMapper;
import com.mysql.DEMO_MYSQL.repository.UserRepository;
import com.mysql.DEMO_MYSQL.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    UserRepository userRepository;
    UserMapper userMapper;

    @PostMapping("/login")
    ApiResponse<UserResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        var user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setToken(result.getToken());
        return ApiResponse.<UserResponse>builder().data(userResponse).success(true).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntroSpectTokenResponse> introSpectToken(@RequestBody IntroSpectTokenRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introSpectToken(request);

        return ApiResponse.<IntroSpectTokenResponse>builder().data(result).success(true).build();
    }
}
