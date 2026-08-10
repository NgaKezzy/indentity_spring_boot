package com.mysql.DEMO_MYSQL.service;

import com.mysql.DEMO_MYSQL.dto.request.authent.AuthenticationRequest;
import com.mysql.DEMO_MYSQL.dto.request.authent.IntroSpectTokenRequest;
import com.mysql.DEMO_MYSQL.dto.request.authent.LogoutRequest;
import com.mysql.DEMO_MYSQL.dto.request.authent.RefreshRequest;
import com.mysql.DEMO_MYSQL.dto.response.authent.AuthenticationResponse;
import com.mysql.DEMO_MYSQL.dto.response.authent.RefreshTokenResponse;
import com.mysql.DEMO_MYSQL.entity.InvalidatedToken;
import com.mysql.DEMO_MYSQL.entity.User;
import com.mysql.DEMO_MYSQL.exception.AppException;
import com.mysql.DEMO_MYSQL.exception.ErrorCode;
import com.mysql.DEMO_MYSQL.repository.InvalidatedTokenRepository;
import com.mysql.DEMO_MYSQL.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    InvalidatedTokenRepository invalidatedTokenRepository;

    @InjectMocks
    AuthenticationService authenticationService;

    User user;
    AuthenticationRequest authenticationRequest;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authenticationService, "SIGNER_KEY", "a".repeat(64));
        ReflectionTestUtils.setField(authenticationService, "VALID_DURATION", 1L);
        ReflectionTestUtils.setField(authenticationService, "REFRESHABLE_DURATION", 7L);

        user = User.builder()
                .id("user-id")
                .userName("ngakezzy")
                .passWord(new BCryptPasswordEncoder(10).encode("12345678"))
                .roles(Set.of())
                .build();
        authenticationRequest = AuthenticationRequest.builder()
                .userName("ngakezzy")
                .passWord("12345678")
                .build();
    }

    @Test
    void authenticate_validCredentials_success() {
        Mockito.when(userRepository.findByUserName("ngakezzy")).thenReturn(Optional.of(user));

        AuthenticationResponse result = authenticationService.authenticate(authenticationRequest);

        assertTrue(result.isAuthenticated());
        assertNotNull(result.getToken());
        assertNotNull(result.getRefreshToken());
    }

    @Test
    void authenticate_userNotFound_fail() {
        Mockito.when(userRepository.findByUserName("ngakezzy")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class,
                () -> authenticationService.authenticate(authenticationRequest));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void authenticate_wrongPassword_fail() {
        authenticationRequest.setPassWord("wrong-password");
        Mockito.when(userRepository.findByUserName("ngakezzy")).thenReturn(Optional.of(user));

        AppException exception = assertThrows(AppException.class,
                () -> authenticationService.authenticate(authenticationRequest));

        assertEquals(ErrorCode.UN_AUTHENTICATED, exception.getErrorCode());
    }

    @Test
    void introSpectToken_invalidToken_returnsFalse() throws Exception {
        var result = authenticationService.introSpectToken(
                IntroSpectTokenRequest.builder().token("invalid-token").build());

        assertFalse(result.isValid());
    }

    @Test
    void refreshToken_validRefreshToken_success() throws Exception {
        Mockito.when(userRepository.findByUserName("ngakezzy")).thenReturn(Optional.of(user));
        AuthenticationResponse authentication = authenticationService.authenticate(authenticationRequest);

        RefreshTokenResponse result = authenticationService.refreshToken(
                RefreshRequest.builder().refreshToken(authentication.getRefreshToken()).build());

        assertNotNull(result.getToken());
        Mockito.verify(userRepository, Mockito.times(2)).findByUserName("ngakezzy");
    }

    @Test
    void refreshToken_blankToken_fail() {
        RefreshRequest request = RefreshRequest.builder().refreshToken(" ").build();

        AppException exception = assertThrows(AppException.class,
                () -> authenticationService.refreshToken(request));

        assertEquals(ErrorCode.UN_AUTHENTICATED, exception.getErrorCode());
    }

    @Test
    void logout_validToken_savesInvalidatedToken() throws Exception {
        Mockito.when(userRepository.findByUserName("ngakezzy")).thenReturn(Optional.of(user));
        AuthenticationResponse authentication = authenticationService.authenticate(authenticationRequest);

        authenticationService.logout(LogoutRequest.builder().token(authentication.getToken()).build());

        ArgumentCaptor<InvalidatedToken> captor = ArgumentCaptor.forClass(InvalidatedToken.class);
        Mockito.verify(invalidatedTokenRepository).save(captor.capture());
        assertNotNull(captor.getValue().getId());
        assertNotNull(captor.getValue().getExpiryTime());
    }
}
