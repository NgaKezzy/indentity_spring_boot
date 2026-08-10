package com.mysql.DEMO_MYSQL.dto.request.authent;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshRequest {
  @NotBlank(message = "REFRESH_TOKEN_INVALID") String refreshToken;
}
