package com.mysql.DEMO_MYSQL.dto.request.role;

import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleUpdateRequest {

  String description;
  Set<String> permissions;
}
