package com.mysql.DEMO_MYSQL.controller;

import com.mysql.DEMO_MYSQL.dto.request.UserCreationRequest;
import com.mysql.DEMO_MYSQL.dto.request.UserUpdateRequest;
import com.mysql.DEMO_MYSQL.dto.response.ApiResponse;
import com.mysql.DEMO_MYSQL.entity.User;
import com.mysql.DEMO_MYSQL.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping()
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<User> response = new ApiResponse<>();
        response.setMessage("User created successfully");
        response.setSuccess(true);
        response.setData(userService.createUser(request));
        return response;
    }

    @GetMapping()
    ApiResponse<List<User>> getUsers() {
        ApiResponse<List<User>> response = new ApiResponse<>();
        response.setMessage("Success");
        response.setSuccess(true);
        response.setData(userService.getUsers());
        return response;
    }

    @GetMapping("/{userId}")
    ApiResponse<User> getUser(@PathVariable("userId") String userId) {
        ApiResponse<User> response = new ApiResponse<>();
        response.setMessage("Success");
        response.setSuccess(true);
        response.setData(userService.getUser(userId));
        return response;
    }

    @PutMapping("/{userId}")
    ApiResponse<User> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, @PathVariable("userId") String userId) {
        ApiResponse<User> response = new ApiResponse<>();
        response.setMessage("Success");
        response.setSuccess(true);
        response.setData(userService.updateUser(userUpdateRequest, userId));
        return response;
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return "Đã xóa thành công!";
    }

}
