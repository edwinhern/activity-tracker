package com.activity.controller.leetcode;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.activity.config.LeetCodeConfig;
import com.activity.repository.leetcode.LeetCodeUserRepository;
import com.activity.service.leetcode.LeetCodeSyncService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1/leetcode/sync")
@RequiredArgsConstructor
@EnableScheduling
public class LeetCodeSyncController {

  private final LeetCodeSyncService leetCodeSyncService;
  private final LeetCodeConfig leetCodeConfig;
  private final LeetCodeUserRepository userRepository;

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationStart() {
    leetCodeSyncService.initializeUser();
    log.info("Initialized LeetCode user on startup");
  }

  @Scheduled(cron = "${leetcode.sync.cron:0 0 */1 * * *}")
  public void syncStaleUsers() {
    try {
      final String username = leetCodeConfig.getApi().getUsername();
      userRepository.findByUsername(username)
          .ifPresent(user -> leetCodeSyncService.syncUserData(user));
      log.info("Successfully completed sync for user: {}", username);
    } catch (final Exception e) {
      log.error("Failed to sync user", e);
    }
  }

  @GetMapping("/stale")
  @Operation(summary = "Sync stale users", description = "Syncs stale users with LeetCode API")
  public ResponseEntity<String> manualSyncStaleUsers() {
    syncStaleUsers();
    return ResponseEntity.ok("Sync initiated");
  }
}
