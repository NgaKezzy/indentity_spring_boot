package com.mysql.DEMO_MYSQL.controller;

import com.mysql.DEMO_MYSQL.dto.request.UserCreationRequest;
import com.mysql.DEMO_MYSQL.dto.response.ApiResponse;
import com.mysql.DEMO_MYSQL.entity.User;
import com.mysql.DEMO_MYSQL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping()
    ApiResponse<User> createUser(@RequestBody UserCreationRequest request) {
        ApiResponse<User> response = new ApiResponse<>();
        response.setMessage("User created successfully");
        response.setData(userService.createUser(request));
        return response;
    }

    @GetMapping()
    ApiResponse<List<User>> getUsers() {
        ApiResponse<List<User>> response = new ApiResponse<>();
        response.setMessage("Success");
        response.setData(userService.getUsers());
        return response;
    }

}
