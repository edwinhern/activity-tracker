package com.activity.config.leetcode.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.activity.model.entity.leetcode.LeetCodeUser;
import com.activity.model.entity.leetcode.SubmissionStats;
import com.activity.model.entity.leetcode.TotalProblems;
import com.activity.model.entity.leetcode.TotalSolved;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LeetCodeResponseMapper {

  public LeetCodeUser toUserStatsResponse(final String username, final JsonNode response) {
    final JsonNode data = response.get("data");
    final JsonNode matchedUser = data.get("matchedUser");
    final JsonNode profile = matchedUser.get("profile");

    // Create the user
    final LeetCodeUser user = LeetCodeUser.builder()
        .username(username)
        .name(profile.get("realName").asText())
        .avatar(profile.get("userAvatar").asText())
        .country(profile.get("countryName").asText())
        .school(profile.get("school").asText())
        .github(matchedUser.get("githubUrl").asText())
        .birthday(profile.get("birthday").asText())
        .totalProblems(new ArrayList<>())
        .totalSolved(new ArrayList<>())
        .submissionStats(new ArrayList<>())
        .build();

    // Create and add TotalProblems
    final TotalProblems totalProblems = new TotalProblems();
    totalProblems.setUser(user);
    final JsonNode allQuestionsCount = data.get("allQuestionsCount");
    totalProblems.setTotalCount(getCountByDifficulty(allQuestionsCount, "All"));
    totalProblems.setEasyCount(getCountByDifficulty(allQuestionsCount, "Easy"));
    totalProblems.setMediumCount(getCountByDifficulty(allQuestionsCount, "Medium"));
    totalProblems.setHardCount(getCountByDifficulty(allQuestionsCount, "Hard"));
    user.getTotalProblems().add(totalProblems);

    // Create and add TotalSolved
    final TotalSolved totalSolved = new TotalSolved();
    totalSolved.setUser(user);
    final JsonNode acSubmissions = matchedUser.get("submitStats").get("acSubmissionNum");
    totalSolved.setTotalCount(getCountByDifficulty(acSubmissions, "All"));
    totalSolved.setEasyCount(getCountByDifficulty(acSubmissions, "Easy"));
    totalSolved.setMediumCount(getCountByDifficulty(acSubmissions, "Medium"));
    totalSolved.setHardCount(getCountByDifficulty(acSubmissions, "Hard"));
    user.getTotalSolved().add(totalSolved);

    // Create and add SubmissionStats
    final SubmissionStats submissionStats = new SubmissionStats();
    submissionStats.setUser(user);
    final JsonNode submitStats = matchedUser.get("submitStats");
    final JsonNode totalSubmissions = submitStats.get("totalSubmissionNum");
    submissionStats.setPoints(matchedUser.get("contributions").get("points").asInt());
    submissionStats.setReputation(profile.get("reputation").asInt());
    submissionStats.setGlobalRanking(profile.get("ranking").asInt());
    submissionStats.setAcceptanceRate(calculateAcceptanceRate(
        getCountByDifficulty(acSubmissions, "All"),
        getCountByDifficulty(totalSubmissions, "All")));
    user.getSubmissionStats().add(submissionStats);

    return user;
  }

  private int getCountByDifficulty(final JsonNode jsonArray, final String difficulty) {
    for (final JsonNode node : jsonArray) {
      if (node.get("difficulty").asText().equals(difficulty)) {
        return node.get("count").asInt();
      }
    }
    return 0;
  }

  private BigDecimal calculateAcceptanceRate(final int accepted, final int total) {
    if (total == 0) {
      return BigDecimal.ZERO;
    }
    return BigDecimal.valueOf(accepted)
        .multiply(BigDecimal.valueOf(100))
        .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
  }
}
