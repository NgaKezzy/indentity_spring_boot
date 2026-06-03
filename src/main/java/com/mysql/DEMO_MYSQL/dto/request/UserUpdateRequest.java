package com.mysql.DEMO_MYSQL.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String passWord;
    String firstName;
    String lastName;
    @JsonFormat(pattern = "yyyy-M-d")
    LocalDate dob;
}
