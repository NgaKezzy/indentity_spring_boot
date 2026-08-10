package com.mysql.DEMO_MYSQL.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mysql.DEMO_MYSQL.entity.Role;
import java.time.LocalDate;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
  String id;
  String userName;
  String firstName;
  String lastName;
  String token;
  String refreshToken;
  LocalDate dob;
  Set<Role> roles;
}
