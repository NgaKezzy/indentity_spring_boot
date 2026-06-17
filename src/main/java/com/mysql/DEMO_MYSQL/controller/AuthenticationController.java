package com.mysql.DEMO_MYSQL.controller;

import com.mysql.DEMO_MYSQL.dto.request.AuthenticationRequest;
import com.mysql.DEMO_MYSQL.dto.request.IntroSpectTokenRequest;
import com.mysql.DEMO_MYSQL.dto.response.ApiResponse;
import com.mysql.DEMO_MYSQL.dto.response.AuthenticationResponse;
import com.mysql.DEMO_MYSQL.dto.response.IntroSpectTokenResponse;
import com.mysql.DEMO_MYSQL.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().data(result).success(true).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntroSpectTokenResponse> introSpectToken(@RequestBody IntroSpectTokenRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introSpectToken(request);
        return ApiResponse.<IntroSpectTokenResponse>builder().data(result).success(true).build();
    }
}
