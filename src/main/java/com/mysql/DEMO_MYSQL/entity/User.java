package com.mysql.DEMO_MYSQL.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@JsonPropertyOrder({"id", "userName", "passWord", "firstName", "lastName", "dob"})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  String userName;
  String passWord;
  String firstName;
  String lastName;
  LocalDate dob;
  @ManyToMany Set<Role> roles;
}
