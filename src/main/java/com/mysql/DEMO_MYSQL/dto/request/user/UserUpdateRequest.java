package com.mysql.DEMO_MYSQL.dto.request.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mysql.DEMO_MYSQL.validator.DobConstraints;
import java.time.LocalDate;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
  String passWord;
  String firstName;
  String lastName;
  List<String> roles;

  @JsonFormat(pattern = "yyyy-M-d")
  @DobConstraints(min = 18, message = "DOB_UNDER_AGE")
  LocalDate dob;
}
