package com.mysql.DEMO_MYSQL.controller;

import com.mysql.DEMO_MYSQL.dto.request.role.RoleRequest;
import com.mysql.DEMO_MYSQL.dto.request.role.RoleUpdateRequest;
import com.mysql.DEMO_MYSQL.dto.response.permission.PermissionResponse;
import com.mysql.DEMO_MYSQL.dto.response.role.RoleResponse;
import com.mysql.DEMO_MYSQL.exception.ErrorCode;
import com.mysql.DEMO_MYSQL.service.RoleService;
import java.util.List;
import java.util.Set;
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

@WebMvcTest(RoleController.class)
@AutoConfigureMockMvc(addFilters = false)
class RoleControllerTest {
  @Autowired MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

  @MockitoBean RoleService roleService;

  RoleRequest request;
  RoleResponse response;

  @BeforeEach
  void setUp() {
    request =
        RoleRequest.builder()
            .name("ADMIN")
            .description("Administrator")
            .permissions(Set.of("USER_READ"))
            .build();
    response =
        RoleResponse.builder()
            .name("ADMIN")
            .description("Administrator")
            .permissions(
                Set.of(
                    PermissionResponse.builder()
                        .name("USER_READ")
                        .description("Read users")
                        .build()))
            .build();
  }

  @Test
  void createRole_validRequest_success() throws Exception {
    Mockito.when(roleService.create(ArgumentMatchers.any())).thenReturn(response);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/roles")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1000))
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Role created successfully"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("ADMIN"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.permissions[0].name").value("USER_READ"));

    Mockito.verify(roleService).create(ArgumentMatchers.any());
  }

  @Test
  void createRole_nameBlank_fail() throws Exception {
    request.setName(" ");

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/roles")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.code").value(ErrorCode.INVALID_REQUEST.getCode()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.message")
                .value(ErrorCode.INVALID_REQUEST.getMessage()));

    Mockito.verifyNoInteractions(roleService);
  }

  @Test
  void getAll_success() throws Exception {
    Mockito.when(roleService.getAll()).thenReturn(List.of(response));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/roles"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1000))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("ADMIN"));

    Mockito.verify(roleService).getAll();
  }

  @Test
  void updateRole_success() throws Exception {
    RoleUpdateRequest updateRequest =
        RoleUpdateRequest.builder()
            .description("Updated administrator")
            .permissions(Set.of("USER_READ"))
            .build();
    RoleResponse updatedResponse =
        RoleResponse.builder()
            .name("ADMIN")
            .description("Updated administrator")
            .permissions(response.getPermissions())
            .build();
    Mockito.when(roleService.updateRole(ArgumentMatchers.any(), Mockito.eq("ADMIN")))
        .thenReturn(updatedResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/roles/{name}", "ADMIN")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1000))
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Update role successfully"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.description").value("Updated administrator"));

    Mockito.verify(roleService).updateRole(ArgumentMatchers.any(), Mockito.eq("ADMIN"));
  }

  @Test
  void delete_success() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.delete("/roles/{name}", "ADMIN"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1000))
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Delete role successfully"));

    Mockito.verify(roleService).delete("ADMIN");
  }
}
