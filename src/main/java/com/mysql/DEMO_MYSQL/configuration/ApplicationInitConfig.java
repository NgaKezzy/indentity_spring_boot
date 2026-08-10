package com.mysql.DEMO_MYSQL.configuration;

import com.mysql.DEMO_MYSQL.entity.Role;
import com.mysql.DEMO_MYSQL.entity.User;
import com.mysql.DEMO_MYSQL.repository.RoleRepository;
import com.mysql.DEMO_MYSQL.repository.UserRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
  PasswordEncoder passwordEncoder;
  RoleRepository roleRepository;

  @Bean
  ApplicationRunner applicationRunner(UserRepository userRepository) {
    return args -> {
      if (userRepository.findByUserName("admin").isEmpty()) {
        // Lấy Role ADMIN entity từ DB (key là tên enum: "ADMIN")
        Optional<Role> adminRole = roleRepository.findById("ADMIN");
        Set<Role> roles = new HashSet<>();
        adminRole.ifPresent(roles::add);

        User user =
            User.builder()
                .userName("admin")
                .passWord(passwordEncoder.encode("admin"))
                .roles(roles)
                .build();
        userRepository.save(user);
        log.warn("Admin user has been created with default password!");
      }
    };
  }
}
