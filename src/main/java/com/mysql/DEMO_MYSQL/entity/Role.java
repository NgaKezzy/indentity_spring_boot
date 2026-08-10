package com.mysql.DEMO_MYSQL.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@JsonPropertyOrder({"name", "description"})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
  @Id String name;
  String description;
  @ManyToMany Set<Permission> permissions;
}
