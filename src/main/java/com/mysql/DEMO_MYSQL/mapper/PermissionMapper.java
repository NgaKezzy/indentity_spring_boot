package com.mysql.DEMO_MYSQL.mapper;

import com.mysql.DEMO_MYSQL.dto.request.PermissionRequest;
import com.mysql.DEMO_MYSQL.dto.request.UserCreationRequest;
import com.mysql.DEMO_MYSQL.dto.request.UserUpdateRequest;
import com.mysql.DEMO_MYSQL.dto.response.PermissionResponse;
import com.mysql.DEMO_MYSQL.dto.response.UserResponse;
import com.mysql.DEMO_MYSQL.entity.Permission;
import com.mysql.DEMO_MYSQL.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest permissionRequest);

    PermissionResponse toPermissionResponse(Permission permission);
}

