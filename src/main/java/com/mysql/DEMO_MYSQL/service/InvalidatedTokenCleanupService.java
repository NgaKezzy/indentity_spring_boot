package com.mysql.DEMO_MYSQL.service;

import com.mysql.DEMO_MYSQL.repository.InvalidatedTokenRepository;
import java.util.Date;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvalidatedTokenCleanupService {

  InvalidatedTokenRepository invalidatedTokenRepository;

  @Transactional
  @Scheduled(
      cron = "${jobs.invalidated-token-cleanup.cron:0 0 0 * * *}",
      zone = "${jobs.invalidated-token-cleanup.zone:Asia/Ho_Chi_Minh}")
  public void deleteExpiredTokens() {
    int deletedCount = invalidatedTokenRepository.deleteExpiredTokens(new Date());
    log.info("Invalidated-token cleanup completed: deleted {} expired token(s)", deletedCount);
  }
}
