package com.mysql.DEMO_MYSQL.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.mysql.DEMO_MYSQL.entity.InvalidatedToken;
import com.mysql.DEMO_MYSQL.entity.Permission;
import com.mysql.DEMO_MYSQL.entity.Role;
import com.mysql.DEMO_MYSQL.entity.User;
import com.mysql.DEMO_MYSQL.repository.InvalidatedTokenRepository;
import com.mysql.DEMO_MYSQL.repository.PermissionRepository;
import com.mysql.DEMO_MYSQL.repository.RoleRepository;
import com.mysql.DEMO_MYSQL.repository.UserRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Testcontainers(disabledWithoutDocker = true)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RepositoryIntegrationTest {

  @Container @ServiceConnection
  static final MySQLContainer MYSQL =
      new MySQLContainer("mysql:8.4")
          .withDatabaseName("identity_test")
          .withUsername("test")
          .withPassword("test");

  @Autowired TestEntityManager entityManager;

  @Autowired UserRepository userRepository;

  @Autowired RoleRepository roleRepository;

  @Autowired PermissionRepository permissionRepository;

  @Autowired InvalidatedTokenRepository invalidatedTokenRepository;

  @Test
  void userRepository_saveAndFindByUserName_success() {
    User user =
        User.builder()
            .userName("integration-user")
            .passWord("encoded-password")
            .firstName("Integration")
            .lastName("Test")
            .dob(LocalDate.of(1999, 2, 12))
            .build();

    User savedUser = userRepository.saveAndFlush(user);
    entityManager.clear();

    assertThat(savedUser.getId()).isNotBlank();
    assertThat(userRepository.existsByUserName("integration-user")).isTrue();
    assertThat(userRepository.findByUserName("integration-user"))
        .isPresent()
        .get()
        .satisfies(
            foundUser -> {
              assertThat(foundUser.getFirstName()).isEqualTo("Integration");
              assertThat(foundUser.getLastName()).isEqualTo("Test");
              assertThat(foundUser.getDob()).isEqualTo(LocalDate.of(1999, 2, 12));
            });
  }

  @Test
  void userRepository_findByUserName_userDoesNotExist_returnsEmpty() {
    assertThat(userRepository.findByUserName("missing-user")).isEmpty();
    assertThat(userRepository.existsByUserName("missing-user")).isFalse();
  }

  @Test
  void permissionRepository_crud_success() {
    Permission permission =
        Permission.builder().name("USER_READ").description("Read users").build();

    permissionRepository.saveAndFlush(permission);
    entityManager.clear();

    assertThat(permissionRepository.existsByName("USER_READ")).isTrue();
    assertThat(permissionRepository.findById("USER_READ"))
        .isPresent()
        .get()
        .extracting(Permission::getDescription)
        .isEqualTo("Read users");

    permissionRepository.deleteById("USER_READ");
    permissionRepository.flush();
    assertThat(permissionRepository.findById("USER_READ")).isEmpty();
  }

  @Test
  void roleRepository_saveWithPermissions_success() {
    Permission permission =
        permissionRepository.saveAndFlush(
            Permission.builder().name("USER_READ").description("Read users").build());
    Role role =
        Role.builder()
            .name("ADMIN")
            .description("Administrator")
            .permissions(Set.of(permission))
            .build();

    roleRepository.saveAndFlush(role);
    entityManager.clear();

    assertThat(roleRepository.existsByName("ADMIN")).isTrue();
    assertThat(roleRepository.findById("ADMIN"))
        .isPresent()
        .get()
        .satisfies(
            foundRole -> {
              assertThat(foundRole.getDescription()).isEqualTo("Administrator");
              assertThat(foundRole.getPermissions())
                  .extracting(Permission::getName)
                  .containsExactly("USER_READ");
            });
  }

  @Test
  void roleRepository_roleDoesNotExist_returnsFalse() {
    assertThat(roleRepository.existsByName("UNKNOWN")).isFalse();
    assertThat(roleRepository.findById("UNKNOWN")).isEmpty();
  }

  @Test
  void invalidatedTokenRepository_deleteExpiredTokens_deletesOnlyExpiredTokens() {
    Instant now = Instant.now();
    InvalidatedToken expiredToken =
        InvalidatedToken.builder()
            .id("expired-token")
            .expiryTime(Date.from(now.minus(1, ChronoUnit.HOURS)))
            .build();
    InvalidatedToken activeToken =
        InvalidatedToken.builder()
            .id("active-token")
            .expiryTime(Date.from(now.plus(1, ChronoUnit.HOURS)))
            .build();
    invalidatedTokenRepository.saveAllAndFlush(Set.of(expiredToken, activeToken));

    int deletedCount = invalidatedTokenRepository.deleteExpiredTokens(Date.from(now));
    invalidatedTokenRepository.flush();
    entityManager.clear();

    assertThat(deletedCount).isEqualTo(1);
    assertThat(invalidatedTokenRepository.findById("expired-token")).isEmpty();
    assertThat(invalidatedTokenRepository.findById("active-token")).isPresent();
  }
}
