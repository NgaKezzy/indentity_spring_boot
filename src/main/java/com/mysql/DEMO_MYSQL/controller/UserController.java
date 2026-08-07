package com.mysql.DEMO_MYSQL.controller;

import com.mysql.DEMO_MYSQL.dto.request.user.UserCreationRequest;
import com.mysql.DEMO_MYSQL.dto.request.user.UserUpdateRequest;
import com.mysql.DEMO_MYSQL.dto.response.ApiResponse;
import com.mysql.DEMO_MYSQL.dto.response.user.UserResponse;
import com.mysql.DEMO_MYSQL.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Users", description = "Quản lý người dùng")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    UserService userService;

    @PostMapping()
    @Operation(summary = "Tạo người dùng")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setMessage("User created successfully");
        response.setSuccess(true);
        response.setCode(1000);
        response.setData(userService.createUser(request));
        return response;
    }

    @GetMapping()
    @Operation(summary = "Lấy danh sách người dùng")
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder().message("Success").code(1000).success(true)
                .data(userService.getUsers()).build();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Lấy thông tin người dùng")
    ApiResponse<UserResponse> getUser(@PathVariable @Parameter(description = "ID người dùng") String userId) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setMessage("Success");
        response.setSuccess(true);
        response.setCode(1000);
        response.setData(userService.getUser(userId));
        return response;
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Cập nhật người dùng")
    ApiResponse<UserResponse> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
                                         @PathVariable @Parameter(description = "ID người dùng") String userId) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setMessage("Success");
        response.setSuccess(true);
        response.setData(userService.updateUser(userUpdateRequest, userId));
        return response;
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Xóa người dùng")
    String deleteUser(@PathVariable @Parameter(description = "ID người dùng") String userId) {
        userService.deleteUser(userId);
        return "Đã xóa thành công!";
    }

}
