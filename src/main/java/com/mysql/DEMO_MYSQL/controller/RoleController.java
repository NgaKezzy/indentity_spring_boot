package com.mysql.DEMO_MYSQL.controller;

import com.mysql.DEMO_MYSQL.dto.request.role.RoleRequest;
import com.mysql.DEMO_MYSQL.dto.request.role.RoleUpdateRequest;
import com.mysql.DEMO_MYSQL.dto.response.ApiResponse;
import com.mysql.DEMO_MYSQL.dto.response.role.RoleResponse;
import com.mysql.DEMO_MYSQL.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Roles", description = "Quản lý vai trò và quyền của vai trò")
public class RoleController {
  RoleService roleService;

  @PostMapping
  @Operation(summary = "Tạo vai trò")
  ApiResponse<RoleResponse> createRole(@Valid @RequestBody RoleRequest request) {
    return ApiResponse.<RoleResponse>builder()
        .message("Role created successfully")
        .success(true)
        .data(roleService.create(request))
        .build();
  }

  @GetMapping
  @Operation(summary = "Lấy danh sách vai trò")
  ApiResponse<List<RoleResponse>> getALl() {

    return ApiResponse.<List<RoleResponse>>builder()
        .message("Get all role successfully")
        .success(true)
        .data(roleService.getAll())
        .build();
  }

  @DeleteMapping("/{name}")
  @Operation(summary = "Xóa vai trò")
  ApiResponse<Void> delete(
      @Parameter(description = "Tên vai trò", example = "ADMIN") @PathVariable String name) {
    roleService.delete(name);
    return ApiResponse.<Void>builder().message("Delete role successfully").success(true).build();
  }

  @PutMapping("/{name}")
  @Operation(summary = "Cập nhật vai trò")
  ApiResponse<RoleResponse> updateRole(
      @RequestBody RoleUpdateRequest roleUpdateRequest,
      @Parameter(description = "Tên vai trò", example = "ADMIN") @PathVariable String name) {
    return ApiResponse.<RoleResponse>builder()
        .message("Update role successfully")
        .success(true)
        .data(roleService.updateRole(roleUpdateRequest, name))
        .build();
  }
}
