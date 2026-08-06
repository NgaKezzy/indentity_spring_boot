package com.mysql.DEMO_MYSQL.repository;

import com.mysql.DEMO_MYSQL.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {

    @Modifying
    @Query("delete from InvalidatedToken token where token.expiryTime <= :now")
    int deleteExpiredTokens(Date now);
}
