package com.mysql.DEMO_MYSQL.mapper;

import com.mysql.DEMO_MYSQL.dto.request.permission.PermissionRequest;
import com.mysql.DEMO_MYSQL.dto.response.permission.PermissionResponse;
import com.mysql.DEMO_MYSQL.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

  Permission toPermission(PermissionRequest permissionRequest);

  PermissionResponse toPermissionResponse(Permission permission);
}
