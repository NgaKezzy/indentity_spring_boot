package com.mysql.DEMO_MYSQL.controller;

import com.mysql.DEMO_MYSQL.dto.request.permission.PermissionRequest;
import com.mysql.DEMO_MYSQL.dto.response.ApiResponse;
import com.mysql.DEMO_MYSQL.dto.response.permission.PermissionResponse;
import com.mysql.DEMO_MYSQL.service.PermissionService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Permissions", description = "Quản lý quyền")
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    @Operation(summary = "Tạo quyền")
    ApiResponse<PermissionResponse> createPermission(
            @Valid @RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder().message("Permission created successfully").success(true)
                .data(permissionService.create(request)).build();
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách quyền")
    ApiResponse<List<PermissionResponse>> getALl() {

        return ApiResponse.<List<PermissionResponse>>builder().message("Get all permissions successfully").success(true)
                .data(permissionService.getAll()).build();
    }

    @DeleteMapping("/{permissionName}")
    @Operation(summary = "Xóa quyền")
    ApiResponse<Void> delete(@Parameter(description = "Tên quyền", example = "USER_READ") @PathVariable String permissionName) {
        permissionService.delete(permissionName);
        return ApiResponse.<Void>builder().message("Delete permission successfully").success(true).build();
    }

}
