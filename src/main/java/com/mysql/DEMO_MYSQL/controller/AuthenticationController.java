package com.mysql.DEMO_MYSQL.controller;

import com.mysql.DEMO_MYSQL.dto.request.AuthenticationRequest;
import com.mysql.DEMO_MYSQL.dto.response.ApiResponse;
import com.mysql.DEMO_MYSQL.dto.response.AuthenticationResponse;
import com.mysql.DEMO_MYSQL.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        boolean result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().code(200)
                .data(AuthenticationResponse.builder().authenticated(result).build()).build();
    }
}
