package com.mysql.DEMO_MYSQL.dto.request.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mysql.DEMO_MYSQL.validator.DobConstraints;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank(message = "USERNAME_INVALID")
    @Size(min = 3, message = "USERNAME_INVALID")
    String userName;
    @NotBlank(message = "PASSWORD_INVALID")
    @Size(min = 8, message = "PASSWORD_TOO_WEAK")
    String passWord;
    String firstName;
    String lastName;
    @JsonFormat(pattern = "yyyy-M-d")
    @DobConstraints(min = 18, message = "DOB_UNDER_AGE")
    LocalDate dob;


}
