package com.mysql.DEMO_MYSQL.controller;

import com.mysql.DEMO_MYSQL.dto.request.RoleRequest;
import com.mysql.DEMO_MYSQL.dto.request.RoleUpdateRequest;
import com.mysql.DEMO_MYSQL.dto.response.ApiResponse;
import com.mysql.DEMO_MYSQL.dto.response.RoleResponse;
import com.mysql.DEMO_MYSQL.service.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createRole(
            @Valid @RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder().message("Role created successfully").success(true)
                .data(roleService.create(request)).build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getALl() {

        return ApiResponse.<List<RoleResponse>>builder().message("Get all role successfully").success(true)
                .data(roleService.getAll()).build();
    }

    @DeleteMapping("/{name}")
    ApiResponse<Void> delete(@PathVariable String name) {
        roleService.delete(name);
        return ApiResponse.<Void>builder().message("Delete role successfully").success(true).build();
    }

    @PutMapping("/{name}")
    ApiResponse<RoleResponse> updateRole(@RequestBody RoleUpdateRequest roleUpdateRequest,
                                         @PathVariable String name) {
        ApiResponse<RoleResponse> response = new ApiResponse<>();
        response.setMessage("Success");
        response.setSuccess(true);
        response.setData(roleService.updateRole(roleUpdateRequest, name));
        return response;
    }

}
