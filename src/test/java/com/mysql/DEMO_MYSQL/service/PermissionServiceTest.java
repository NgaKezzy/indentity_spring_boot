package com.mysql.DEMO_MYSQL.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mysql.DEMO_MYSQL.dto.request.permission.PermissionRequest;
import com.mysql.DEMO_MYSQL.dto.response.permission.PermissionResponse;
import com.mysql.DEMO_MYSQL.entity.Permission;
import com.mysql.DEMO_MYSQL.exception.AppException;
import com.mysql.DEMO_MYSQL.exception.ErrorCode;
import com.mysql.DEMO_MYSQL.mapper.PermissionMapper;
import com.mysql.DEMO_MYSQL.repository.PermissionRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {
  @Mock PermissionRepository permissionRepository;

  @Mock PermissionMapper permissionMapper;

  @InjectMocks PermissionService permissionService;

  PermissionRequest request;
  Permission permission;
  PermissionResponse response;

  @BeforeEach
  void setUp() {
    request = PermissionRequest.builder().name("USER_READ").description("Read users").build();
    permission = Permission.builder().name("USER_READ").description("Read users").build();
    response = PermissionResponse.builder().name("USER_READ").description("Read users").build();
  }

  @Test
  void create_validRequest_success() {
    Mockito.when(permissionRepository.existsByName("USER_READ")).thenReturn(false);
    Mockito.when(permissionMapper.toPermission(request)).thenReturn(permission);
    Mockito.when(permissionRepository.save(permission)).thenReturn(permission);
    Mockito.when(permissionMapper.toPermissionResponse(permission)).thenReturn(response);

    PermissionResponse result = permissionService.create(request);

    assertSame(response, result);
    Mockito.verify(permissionRepository).save(permission);
  }

  @Test
  void create_permissionExisted_fail() {
    Mockito.when(permissionRepository.existsByName("USER_READ")).thenReturn(true);

    AppException exception =
        assertThrows(AppException.class, () -> permissionService.create(request));

    assertEquals(ErrorCode.PERMISSION_EXISTED, exception.getErrorCode());
    Mockito.verify(permissionRepository, Mockito.never()).save(Mockito.any());
  }

  @Test
  void getAll_success() {
    Mockito.when(permissionRepository.findAll()).thenReturn(List.of(permission));
    Mockito.when(permissionMapper.toPermissionResponse(permission)).thenReturn(response);

    List<PermissionResponse> result = permissionService.getAll();

    assertEquals(List.of(response), result);
  }

  @Test
  void delete_success() {
    permissionService.delete("USER_READ");

    Mockito.verify(permissionRepository).deleteById("USER_READ");
  }
}
