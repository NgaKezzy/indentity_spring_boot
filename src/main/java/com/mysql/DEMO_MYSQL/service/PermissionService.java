package com.mysql.DEMO_MYSQL.service;

import com.mysql.DEMO_MYSQL.dto.request.PermissionRequest;
import com.mysql.DEMO_MYSQL.dto.response.PermissionResponse;
import com.mysql.DEMO_MYSQL.entity.Permission;
import com.mysql.DEMO_MYSQL.exception.AppException;
import com.mysql.DEMO_MYSQL.exception.ErrorCode;
import com.mysql.DEMO_MYSQL.mapper.PermissionMapper;
import com.mysql.DEMO_MYSQL.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;


    public PermissionResponse create(PermissionRequest request) {
        if (permissionRepository.existsByName(request.getName()))
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permissionName) {
        permissionRepository.deleteById(permissionName);
    }
}
