package com.mysql.DEMO_MYSQL.controller;

import com.mysql.DEMO_MYSQL.dto.request.user.UserCreationRequest;
import com.mysql.DEMO_MYSQL.dto.response.user.UserResponse;
import com.mysql.DEMO_MYSQL.exception.ErrorCode;
import com.mysql.DEMO_MYSQL.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;


    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;
    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(1999, 02, 12);
        userCreationRequest = UserCreationRequest.builder().userName("ngakezzy").firstName("Nga").lastName("Nguyen")
                .passWord("12345678")
                .dob(
                        dob).build();

        userResponse = UserResponse.builder().id("465156486421cf")
                .userName("ngakezzy").firstName("Nga").lastName("Nguyen")
                .dob(dob).build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        String content = objectMapper.writeValueAsString(userCreationRequest);
        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);

        // WHEN , THEN
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON_VALUE).content(content))
                .andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("465156486421cf"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userName").value("ngakezzy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName").value("Nga"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName").value("Nguyen"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.dob").value(dob.toString()));

    }

    @Test
    void createUser_userNameBlank_fail() throws Exception {
        userCreationRequest.setUserName(" ");

        performCreateUserAndExpectValidationError(ErrorCode.USERNAME_INVALID);
    }

    @Test
    void createUser_userNameTooShort_fail() throws Exception {
        userCreationRequest.setUserName("ab");

        performCreateUserAndExpectValidationError(ErrorCode.USERNAME_INVALID);
    }

    @Test
    void createUser_passWordMissing_fail() throws Exception {
        userCreationRequest.setPassWord(null);

        performCreateUserAndExpectValidationError(ErrorCode.PASSWORD_INVALID);
    }

    @Test
    void createUser_passWordTooWeak_fail() throws Exception {
        userCreationRequest.setPassWord("1234567");

        performCreateUserAndExpectValidationError(ErrorCode.PASSWORD_TOO_WEAK);
    }

    @Test
    void createUser_dobUnder18_fail() throws Exception {
        userCreationRequest.setDob(LocalDate.now().minusYears(18).plusDays(1));

        performCreateUserAndExpectValidationError(ErrorCode.DOB_UNDER_AGE);
    }

    private void performCreateUserAndExpectValidationError(ErrorCode expectedError) throws Exception {
        String content = objectMapper.writeValueAsString(userCreationRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(expectedError.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(resolveExpectedMessage(expectedError)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());

        Mockito.verifyNoInteractions(userService);
    }

    private String resolveExpectedMessage(ErrorCode errorCode) {
        return switch (errorCode) {
            case USERNAME_INVALID -> errorCode.getMessage().replace("{min}", "3");
            case PASSWORD_TOO_WEAK -> errorCode.getMessage().replace("{min}", "8");
            case DOB_UNDER_AGE -> errorCode.getMessage().replace("{min}", "18");
            default -> errorCode.getMessage();
        };
    }
}
