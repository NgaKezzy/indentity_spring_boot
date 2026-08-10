package com.mysql.DEMO_MYSQL.dto.request.role;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
  @NotBlank(message = "Role name không được để trống") String name;

  String description;
  Set<String> permissions;
}
