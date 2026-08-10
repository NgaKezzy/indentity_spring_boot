package com.mysql.DEMO_MYSQL.controller;

import com.mysql.DEMO_MYSQL.dto.request.authent.AuthenticationRequest;
import com.mysql.DEMO_MYSQL.dto.request.authent.IntroSpectTokenRequest;
import com.mysql.DEMO_MYSQL.dto.request.authent.LogoutRequest;
import com.mysql.DEMO_MYSQL.dto.request.authent.RefreshRequest;
import com.mysql.DEMO_MYSQL.dto.response.authent.IntroSpectTokenResponse;
import com.mysql.DEMO_MYSQL.dto.response.authent.RefreshTokenResponse;
import com.mysql.DEMO_MYSQL.dto.response.user.UserResponse;
import com.mysql.DEMO_MYSQL.exception.ErrorCode;
import com.mysql.DEMO_MYSQL.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AuthenticationService authenticationService;

    @Test
    void authenticate_success() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .userName("ngakezzy")
                .passWord("12345678")
                .build();
        UserResponse userResponse = UserResponse.builder()
                .id("user-id")
                .userName("ngakezzy")
                .token("access-token")
                .refreshToken("refresh-token")
                .build();
        Mockito.when(authenticationService.authenticate(ArgumentMatchers.any()))
                .thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("user-id"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userName").value("ngakezzy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").value("access-token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.refreshToken").value("refresh-token"));

        Mockito.verify(authenticationService).authenticate(ArgumentMatchers.any());
    }

    @Test
    void introspectToken_success() throws Exception {
        IntroSpectTokenRequest request = IntroSpectTokenRequest.builder().token("access-token").build();
        Mockito.when(authenticationService.introSpectToken(ArgumentMatchers.any()))
                .thenReturn(IntroSpectTokenResponse.builder().valid(true).build());

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/introspect")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.valid").value(true));

        Mockito.verify(authenticationService).introSpectToken(ArgumentMatchers.any());
    }

    @Test
    void logout_success() throws Exception {
        LogoutRequest request = LogoutRequest.builder().token("access-token").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());

        Mockito.verify(authenticationService).logout(ArgumentMatchers.any());
    }

    @Test
    void refreshToken_success() throws Exception {
        RefreshRequest request = RefreshRequest.builder().refreshToken("refresh-token").build();
        Mockito.when(authenticationService.refreshToken(ArgumentMatchers.any()))
                .thenReturn(RefreshTokenResponse.builder().token("new-access-token").build());

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").value("new-access-token"));

        Mockito.verify(authenticationService).refreshToken(ArgumentMatchers.any());
    }

    @Test
    void refreshToken_blankToken_fail() throws Exception {
        RefreshRequest request = RefreshRequest.builder().refreshToken(" ").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(ErrorCode.REFRESH_TOKEN_INVALID.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.REFRESH_TOKEN_INVALID.getMessage()));

        Mockito.verifyNoInteractions(authenticationService);
    }
}
