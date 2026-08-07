package com.mysql.DEMO_MYSQL.controller;

import com.mysql.DEMO_MYSQL.dto.request.user.UserCreationRequest;
import com.mysql.DEMO_MYSQL.dto.response.user.UserResponse;
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
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1000"));

    }
}
