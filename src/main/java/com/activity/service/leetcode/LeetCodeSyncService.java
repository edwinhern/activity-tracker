package com.activity.service.leetcode;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.activity.config.LeetCodeConfig;
import com.activity.model.ServiceResponse;
import com.activity.model.dto.leetcode.UserStatsResponse;
import com.activity.model.entity.leetcode.LeetCodeUser;
import com.activity.repository.leetcode.LeetCodeUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LeetCodeSyncService {
  private final LeetCodeUserRepository userRepository;
  private final LeetCodeApiClient leetCodeApiClient;
  private final LeetCodeConfig leetCodeConfig;
  private static final int BATCH_SIZE = 100;

  @Transactional
  public void syncStaleUsers() {
    final ZonedDateTime staleThreshold = ZonedDateTime.now().minusHours(1);
    userRepository.findStaleUsers(staleThreshold, BATCH_SIZE)
        .forEach(this::syncUserData);
  }

  @Transactional
  public ServiceResponse<UserStatsResponse> syncUserData(final LeetCodeUser user) {
    try {
      log.info("Syncing data for user: {}", user.getUsername());
      final UserStatsResponse userStats = leetCodeApiClient.fetchUserStats(user.getUsername());
      updateUserData(user, userStats);
      return ServiceResponse.success("Successfully synced user data", userStats);
    } catch (final Exception e) {
      log.error("Error syncing data for user: {}", user.getUsername(), e);
      return ServiceResponse.failure("Failed to sync user data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Transactional
  public ServiceResponse<UserStatsResponse> getUserStats(final String username) {
    try {
      return userRepository.findByUsername(username)
          .map(user -> ServiceResponse.success(
              "User stats retrieved successfully",
              mapToUserStatsResponse(user)))
          .orElse(ServiceResponse.failure(
              "User not found",
              HttpStatus.NOT_FOUND));
    } catch (final Exception e) {
      log.error("Error retrieving user stats for: {}", username, e);
      return ServiceResponse.failure(
          "Failed to retrieve user stats: " + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private void updateUserData(final LeetCodeUser user, final UserStatsResponse stats) {
    user.setTotalSolved(stats.getProblemsSolved().getTotalSolved());
    user.setTotalQuestions(stats.getProblemsSolved().getTotalQuestions());
    user.setTotalEasy(stats.getProblemsSolved().getTotalEasy());
    user.setTotalMedium(stats.getProblemsSolved().getTotalMedium());
    user.setTotalHard(stats.getProblemsSolved().getTotalHard());

    user.setEasySolved(stats.getProblemsSolved().getEasySolved());
    user.setMediumSolved(stats.getProblemsSolved().getMediumSolved());
    user.setHardSolved(stats.getProblemsSolved().getHardSolved());
    user.setAcceptanceRate(stats.getProblemsSolved().getAcceptanceRate());
    user.setContributionPoints(stats.getContributionStats().getPoints());
    user.setReputation(stats.getContributionStats().getReputation());
    user.setGlobalRanking(stats.getContributionStats().getGlobalRanking());
    userRepository.save(user);
  }

  private UserStatsResponse mapToUserStatsResponse(final LeetCodeUser user) {
    return UserStatsResponse.builder()
        .username(user.getUsername())
        .problemsSolved(UserStatsResponse.ProblemsSolved.builder()
            .totalQuestions(user.getTotalSolved())
            .totalSolved(user.getTotalSolved())
            .easySolved(user.getEasySolved())
            .totalEasy(user.getTotalEasy())
            .mediumSolved(user.getMediumSolved())
            .totalMedium(user.getTotalMedium())
            .hardSolved(user.getHardSolved())
            .totalHard(user.getTotalHard())
            .acceptanceRate(user.getAcceptanceRate())
            .build())
        .contributionStats(UserStatsResponse.ContributionStats.builder()
            .points(user.getContributionPoints())
            .reputation(user.getReputation())
            .globalRanking(user.getGlobalRanking())
            .build())
        .build();
  }

  @Transactional
  public LeetCodeUser initializeUser() {
    final String configuredUsername = leetCodeConfig.getApi().getUsername();
    return userRepository.findByUsername(configuredUsername)
        .orElseGet(() -> {
          final LeetCodeUser newUser = new LeetCodeUser();
          newUser.setUsername(configuredUsername);
          return userRepository.save(newUser);
        });
  }
}
