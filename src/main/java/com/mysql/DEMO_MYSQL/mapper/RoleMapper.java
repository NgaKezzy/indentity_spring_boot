package com.mysql.DEMO_MYSQL.mapper;

import com.mysql.DEMO_MYSQL.dto.request.role.RoleRequest;
import com.mysql.DEMO_MYSQL.dto.response.role.RoleResponse;
import com.mysql.DEMO_MYSQL.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
  @Mapping(target = "permissions", ignore = true)
  Role toRole(RoleRequest request);

  RoleResponse toRoleResponse(Role role);
}
