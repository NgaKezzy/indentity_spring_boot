package com.mysql.DEMO_MYSQL.mapper;

import com.mysql.DEMO_MYSQL.dto.request.RoleRequest;
import com.mysql.DEMO_MYSQL.dto.response.RoleResponse;
import com.mysql.DEMO_MYSQL.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}

