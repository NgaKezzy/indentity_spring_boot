package com.mysql.DEMO_MYSQL.controller;

import com.mysql.DEMO_MYSQL.dto.request.PermissionRequest;
import com.mysql.DEMO_MYSQL.dto.response.ApiResponse;
import com.mysql.DEMO_MYSQL.dto.response.PermissionResponse;
import com.mysql.DEMO_MYSQL.service.PermissionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(
            @Valid @RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder().message("Permission created successfully").success(true)
                .data(permissionService.create(request)).build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getALl() {

        return ApiResponse.<List<PermissionResponse>>builder().message("Get all permissions successfully").success(true)
                .data(permissionService.getAll()).build();
    }

    @DeleteMapping("/{permissionName}")
    ApiResponse<Void> delete(@PathVariable String permissionName) {
        permissionService.delete(permissionName);
        return ApiResponse.<Void>builder().message("Delete permission successfully").success(true).build();
    }

}
