package com.mysql.DEMO_MYSQL.service;

import com.mysql.DEMO_MYSQL.dto.request.role.RoleRequest;
import com.mysql.DEMO_MYSQL.dto.request.role.RoleUpdateRequest;
import com.mysql.DEMO_MYSQL.dto.response.role.RoleResponse;
import com.mysql.DEMO_MYSQL.entity.Permission;
import com.mysql.DEMO_MYSQL.entity.Role;
import com.mysql.DEMO_MYSQL.exception.AppException;
import com.mysql.DEMO_MYSQL.exception.ErrorCode;
import com.mysql.DEMO_MYSQL.mapper.RoleMapper;
import com.mysql.DEMO_MYSQL.repository.PermissionRepository;
import com.mysql.DEMO_MYSQL.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock
    RoleRepository roleRepository;

    @Mock
    PermissionRepository permissionRepository;

    @Mock
    RoleMapper roleMapper;

    @InjectMocks
    RoleService roleService;

    RoleRequest request;
    Permission permission;
    Role role;
    RoleResponse response;

    @BeforeEach
    void setUp() {
        request = RoleRequest.builder()
                .name("ADMIN")
                .description("Administrator")
                .permissions(Set.of("USER_READ"))
                .build();
        permission = Permission.builder().name("USER_READ").build();
        role = Role.builder().name("ADMIN").description("Administrator").build();
        response = RoleResponse.builder().name("ADMIN").description("Administrator").build();
    }

    @Test
    void create_validRequest_success() {
        Mockito.when(roleRepository.existsByName("ADMIN")).thenReturn(false);
        Mockito.when(permissionRepository.findAllById(request.getPermissions())).thenReturn(List.of(permission));
        Mockito.when(roleMapper.toRole(request)).thenReturn(role);
        Mockito.when(roleRepository.save(role)).thenReturn(role);
        Mockito.when(roleMapper.toRoleResponse(role)).thenReturn(response);

        RoleResponse result = roleService.create(request);

        assertSame(response, result);
        assertEquals(Set.of(permission), role.getPermissions());
    }

    @Test
    void create_roleExisted_fail() {
        Mockito.when(roleRepository.existsByName("ADMIN")).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> roleService.create(request));

        assertEquals(ErrorCode.PERMISSION_EXISTED, exception.getErrorCode());
        Mockito.verify(roleRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void getAll_success() {
        Mockito.when(roleRepository.findAll()).thenReturn(List.of(role));
        Mockito.when(roleMapper.toRoleResponse(role)).thenReturn(response);

        List<RoleResponse> result = roleService.getAll();

        assertEquals(List.of(response), result);
    }

    @Test
    void updateRole_roleFound_success() {
        RoleUpdateRequest updateRequest = RoleUpdateRequest.builder()
                .description("Updated")
                .permissions(Set.of("USER_READ"))
                .build();
        Mockito.when(roleRepository.findById("ADMIN")).thenReturn(Optional.of(role));
        Mockito.when(permissionRepository.findAllById(updateRequest.getPermissions()))
                .thenReturn(List.of(permission));
        Mockito.when(roleRepository.save(role)).thenReturn(role);
        Mockito.when(roleMapper.toRoleResponse(role)).thenReturn(response);

        RoleResponse result = roleService.updateRole(updateRequest, "ADMIN");

        assertSame(response, result);
        assertEquals("Updated", role.getDescription());
        assertEquals(Set.of(permission), role.getPermissions());
    }

    @Test
    void updateRole_roleNotFound_fail() {
        Mockito.when(roleRepository.findById("UNKNOWN")).thenReturn(Optional.empty());
        RoleUpdateRequest updateRequest = RoleUpdateRequest.builder().build();

        AppException exception = assertThrows(AppException.class,
                () -> roleService.updateRole(updateRequest, "UNKNOWN"));

        assertEquals(ErrorCode.ROLE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void delete_success() {
        roleService.delete("ADMIN");

        Mockito.verify(roleRepository).deleteById("ADMIN");
    }
}
