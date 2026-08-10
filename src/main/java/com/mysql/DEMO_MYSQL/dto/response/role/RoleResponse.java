package com.mysql.DEMO_MYSQL.dto.response.role;

import com.mysql.DEMO_MYSQL.dto.response.permission.PermissionResponse;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
  String name;
  String description;
  Set<PermissionResponse> permissions;
}
