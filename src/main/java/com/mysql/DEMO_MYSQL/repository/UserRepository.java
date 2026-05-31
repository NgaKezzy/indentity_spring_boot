package com.mysql.DEMO_MYSQL.repository;

import com.mysql.DEMO_MYSQL.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByUserName(String username);
}
