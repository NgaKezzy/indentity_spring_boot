package com.mysql.DEMO_MYSQL.service;

import com.mysql.DEMO_MYSQL.dto.request.AuthenticationRequest;
import com.mysql.DEMO_MYSQL.exception.AppException;
import com.mysql.DEMO_MYSQL.exception.ErrorCode;
import com.mysql.DEMO_MYSQL.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    public boolean authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(request.getPassWord(), user.getPassWord());

    }
}
