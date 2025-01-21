package com.activity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.activity.config.leetcode.LeetCodeConfig;
import com.activity.model.ServiceResponse;
import com.activity.model.entity.leetcode.LeetCodeUser;
import com.activity.service.LeetCodeSyncService;

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

  @Scheduled(cron = "${leetcode.sync.cron:0 0 */1 * * *}")
  @GetMapping("/")
  @Operation(summary = "Sync Leetcode stats", description = "Syncs user stats with LeetCode API")
  public ServiceResponse<LeetCodeUser> syncUserStats() {
    try {
      final String username = leetCodeConfig.getApi().getUsername();

      if (username == null) {
        log.error("Username is not set in the configuration");
        return ServiceResponse.failure("Username is not set in the configuration", HttpStatus.INTERNAL_SERVER_ERROR);
      }

      return leetCodeSyncService.syncUserData(username);
    } catch (final Exception e) {
      log.error("Failed to sync user", e);
      return ServiceResponse.failure("Failed to sync user");
    }
  }
}
