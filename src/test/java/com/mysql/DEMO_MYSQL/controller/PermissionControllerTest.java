package com.mysql.DEMO_MYSQL.controller;

import com.mysql.DEMO_MYSQL.dto.request.permission.PermissionRequest;
import com.mysql.DEMO_MYSQL.dto.response.permission.PermissionResponse;
import com.mysql.DEMO_MYSQL.exception.ErrorCode;
import com.mysql.DEMO_MYSQL.service.PermissionService;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;

@WebMvcTest(PermissionController.class)
@AutoConfigureMockMvc(addFilters = false)
class PermissionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    PermissionService permissionService;

    PermissionRequest request;
    PermissionResponse response;

    @BeforeEach
    void setUp() {
        request = PermissionRequest.builder()
                .name("USER_READ")
                .description("Read users")
                .build();
        response = PermissionResponse.builder()
                .name("USER_READ")
                .description("Read users")
                .build();
    }

    @Test
    void createPermission_validRequest_success() throws Exception {
        Mockito.when(permissionService.create(ArgumentMatchers.any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/permissions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Permission created successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("USER_READ"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value("Read users"));

        Mockito.verify(permissionService).create(ArgumentMatchers.any());
    }

    @Test
    void createPermission_nameBlank_fail() throws Exception {
        request.setName(" ");

        mockMvc.perform(MockMvcRequestBuilders.post("/permissions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(ErrorCode.INVALID_REQUEST.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.INVALID_REQUEST.getMessage()));

        Mockito.verifyNoInteractions(permissionService);
    }

    @Test
    void getAll_success() throws Exception {
        Mockito.when(permissionService.getAll()).thenReturn(List.of(response));

        mockMvc.perform(MockMvcRequestBuilders.get("/permissions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("USER_READ"));

        Mockito.verify(permissionService).getAll();
    }

    @Test
    void delete_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/permissions/{permissionName}", "USER_READ"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Delete permission successfully"));

        Mockito.verify(permissionService).delete("USER_READ");
    }
}
