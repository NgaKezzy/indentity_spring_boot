package com.mysql.DEMO_MYSQL.configuration;

import com.mysql.DEMO_MYSQL.entity.User;
import com.mysql.DEMO_MYSQL.enums.Role;
import com.mysql.DEMO_MYSQL.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    private PasswordEncoder passwordEncoder;


    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUserName("admin").isEmpty()) {
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());
                User user = User.builder().userName("admin").passWord(passwordEncoder.encode("admin"))
//                        .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("admin user has been create with default!");
            }
        };
    }
}
