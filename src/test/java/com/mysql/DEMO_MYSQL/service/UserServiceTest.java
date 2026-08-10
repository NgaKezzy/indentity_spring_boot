package com.mysql.DEMO_MYSQL.service;

import com.mysql.DEMO_MYSQL.dto.request.user.UserCreationRequest;
import com.mysql.DEMO_MYSQL.dto.request.user.UserUpdateRequest;
import com.mysql.DEMO_MYSQL.dto.response.user.UserResponse;
import com.mysql.DEMO_MYSQL.entity.Role;
import com.mysql.DEMO_MYSQL.entity.User;
import com.mysql.DEMO_MYSQL.exception.AppException;
import com.mysql.DEMO_MYSQL.exception.ErrorCode;
import com.mysql.DEMO_MYSQL.mapper.UserMapper;
import com.mysql.DEMO_MYSQL.repository.RoleRepository;
import com.mysql.DEMO_MYSQL.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    UserCreationRequest creationRequest;
    User user;
    UserResponse response;
    Role userRole;

    @BeforeEach
    void setUp() {
        creationRequest = UserCreationRequest.builder()
                .userName("ngakezzy")
                .passWord("12345678")
                .firstName("Nga")
                .lastName("Nguyen")
                .dob(LocalDate.of(1999, 2, 12))
                .build();
        user = User.builder().id("user-id").userName("ngakezzy").build();
        response = UserResponse.builder().id("user-id").userName("ngakezzy").build();
        userRole = Role.builder().name("USER").build();
    }

    @Test
    void createUser_validRequest_success() {
        Mockito.when(userRepository.existsByUserName("ngakezzy")).thenReturn(false);
        Mockito.when(userMapper.toUser(creationRequest)).thenReturn(user);
        Mockito.when(passwordEncoder.encode("12345678")).thenReturn("encoded-password");
        Mockito.when(roleRepository.findById("USER")).thenReturn(Optional.of(userRole));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(response);

        UserResponse result = userService.createUser(creationRequest);

        assertSame(response, result);
        assertEquals("encoded-password", user.getPassWord());
        assertEquals(Set.of(userRole), user.getRoles());
    }

    @Test
    void createUser_userExisted_fail() {
        Mockito.when(userRepository.existsByUserName("ngakezzy")).thenReturn(true);

        AppException exception = assertThrows(AppException.class,
                () -> userService.createUser(creationRequest));

        assertEquals(ErrorCode.USER_EXISTED, exception.getErrorCode());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void createUser_defaultRoleNotFound_fail() {
        Mockito.when(userRepository.existsByUserName("ngakezzy")).thenReturn(false);
        Mockito.when(userMapper.toUser(creationRequest)).thenReturn(user);
        Mockito.when(passwordEncoder.encode("12345678")).thenReturn("encoded-password");
        Mockito.when(roleRepository.findById("USER")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class,
                () -> userService.createUser(creationRequest));

        assertEquals(ErrorCode.ROLE_NOT_FOUND, exception.getErrorCode());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void getUsers_success() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(response);

        List<UserResponse> result = userService.getUsers();

        assertEquals(List.of(response), result);
    }

    @Test
    void getUser_userFound_success() {
        Mockito.when(userRepository.findById("user-id")).thenReturn(Optional.of(user));
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(response);

        UserResponse result = userService.getUser("user-id");

        assertSame(response, result);
    }

    @Test
    void getUser_userNotFound_fail() {
        Mockito.when(userRepository.findById("unknown")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUser("unknown"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void updateUser_userFound_success() {
        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .passWord("new-password")
                .firstName("Updated")
                .roles(List.of("ADMIN"))
                .build();
        Role adminRole = Role.builder().name("ADMIN").build();
        Mockito.when(userRepository.findById("user-id")).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.encode("new-password")).thenReturn("encoded-new-password");
        Mockito.when(roleRepository.findAllById(updateRequest.getRoles())).thenReturn(List.of(adminRole));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(response);

        UserResponse result = userService.updateUser(updateRequest, "user-id");

        assertSame(response, result);
        assertEquals("encoded-new-password", user.getPassWord());
        assertEquals(Set.of(adminRole), user.getRoles());
        Mockito.verify(userMapper).updateUser(user, updateRequest);
    }

    @Test
    void updateUser_userNotFound_fail() {
        Mockito.when(userRepository.findById("unknown")).thenReturn(Optional.empty());
        UserUpdateRequest updateRequest = UserUpdateRequest.builder().build();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser(updateRequest, "unknown"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void deleteUser_success() {
        userService.deleteUser("user-id");

        Mockito.verify(userRepository).deleteById("user-id");
    }
}
