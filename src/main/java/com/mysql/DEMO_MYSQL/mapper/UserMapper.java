package com.mysql.DEMO_MYSQL.mapper;

import com.mysql.DEMO_MYSQL.dto.request.UserCreationRequest;
import com.mysql.DEMO_MYSQL.dto.request.UserUpdateRequest;
import com.mysql.DEMO_MYSQL.dto.response.UserResponse;
import com.mysql.DEMO_MYSQL.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Bỏ qua roles — service set roles thủ công sau khi map
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest userCreationRequest);

    UserResponse toUserResponse(User user);

    // Bỏ qua roles — service set roles thủ công sau khi map
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
