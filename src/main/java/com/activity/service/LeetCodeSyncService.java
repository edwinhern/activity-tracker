package com.activity.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.activity.config.leetcode.LeetCodeClient;
import com.activity.model.ServiceResponse;
import com.activity.model.entity.leetcode.LeetCodeUser;
import com.activity.model.entity.leetcode.SubmissionStats;
import com.activity.model.entity.leetcode.TotalProblems;
import com.activity.model.entity.leetcode.TotalSolved;
import com.activity.repository.LeetCodeUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LeetCodeSyncService {
  private final LeetCodeUserRepository userRepository;
  private final LeetCodeClient leetCodeClient;

  @Transactional
  public ServiceResponse<LeetCodeUser> syncUserData(final String username) {
    try {
      log.info("Syncing data for user: {}", username);

      // First, get or create the user
      LeetCodeUser user = userRepository.findByUsername(username)
          .orElseGet(() -> LeetCodeUser.builder()
              .username(username)
              .build());

      final LeetCodeUser latestStats = leetCodeClient.fetchUserStats(username);

      // Keep only the latest stats
      user.getTotalProblems().clear();
      user.getTotalSolved().clear();
      user.getSubmissionStats().clear();

      // Update user profile information
      updateUserProfile(user, latestStats);
      updateUserStats(user, latestStats);

      user = userRepository.save(user);

      // Clear collections to prevent circular references in response
      user.getTotalProblems().forEach(tp -> tp.setUser(null));
      user.getTotalSolved().forEach(ts -> ts.setUser(null));
      user.getSubmissionStats().forEach(ss -> ss.setUser(null));

      return ServiceResponse.success("Successfully synced user data", user);
    } catch (final Exception e) {
      log.error("Error syncing data for user: {}", username, e);
      return ServiceResponse.failure("Failed to sync user data: " + e.getMessage());
    }
  }

  @Transactional(readOnly = true)
  public ServiceResponse<LeetCodeUser> getUserStats(final String username) {
    try {
      final Optional<LeetCodeUser> user = userRepository.findByUsernameWithStats(username);

      if (user.isEmpty()) {
        return ServiceResponse.failure("User not found", HttpStatus.NOT_FOUND);
      }

      return ServiceResponse.success("User stats retrieved successfully", user.get());
    } catch (final Exception e) {
      final String errorMessage = "Failed to retrieve user stats: " + e.getMessage();
      log.error(errorMessage, e);
      return ServiceResponse.failure(errorMessage);
    }
  }

  private void updateUserProfile(final LeetCodeUser existingUser, final LeetCodeUser latestStats) {
    existingUser.setName(latestStats.getName());
    existingUser.setAvatar(latestStats.getAvatar());
    existingUser.setCountry(latestStats.getCountry());
    existingUser.setSchool(latestStats.getSchool());
    existingUser.setGithub(latestStats.getGithub());
    existingUser.setBirthday(latestStats.getBirthday());
  }

  private void updateUserStats(final LeetCodeUser user, final LeetCodeUser latestStats) {
    if (!latestStats.getTotalProblems().isEmpty()) {
      final TotalProblems newProblems = latestStats.getTotalProblems().get(0);
      newProblems.setUser(user);
      user.getTotalProblems().add(newProblems);
    }

    if (!latestStats.getTotalSolved().isEmpty()) {
      final TotalSolved newSolved = latestStats.getTotalSolved().get(0);
      newSolved.setUser(user);
      user.getTotalSolved().add(newSolved);
    }

    if (!latestStats.getSubmissionStats().isEmpty()) {
      final SubmissionStats newStats = latestStats.getSubmissionStats().get(0);
      newStats.setUser(user);
      user.getSubmissionStats().add(newStats);
    }
  }
}
