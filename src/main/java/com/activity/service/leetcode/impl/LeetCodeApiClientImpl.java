package com.activity.service.leetcode.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.activity.exception.LeetCodeApiException;
import com.activity.model.dto.leetcode.UserStatsResponse;
import com.activity.service.leetcode.LeetCodeApiClient;
import com.activity.service.leetcode.graphql.LeetCodeQueries;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class LeetCodeApiClientImpl implements LeetCodeApiClient {
  private final WebClient leetCodeWebClient;

  @Override
  @Retryable(maxAttempts = 3)
  public UserStatsResponse fetchUserStats(final String username) {
    log.info("Fetching stats for user: {}", username);

    final var response = executeGraphQLQuery(LeetCodeQueries.USER_PROFILE,
        Map.of("username", username))
        .block();

    if (response == null || !response.has("data")) {
      throw new LeetCodeApiException("Failed to fetch user stats");
    }

    return mapToUserStatsResponse(username, response.get("data"));
  }

  private Mono<JsonNode> executeGraphQLQuery(final String query, final Map<String, Object> variables) {
    return leetCodeWebClient.post()
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(Map.of(
            "query", query,
            "variables", variables))
        .retrieve()
        .bodyToMono(JsonNode.class)
        .doOnError(error -> log.error("GraphQL query failed", error));
  }

  private UserStatsResponse mapToUserStatsResponse(final String username, final JsonNode data) {
    final var matchedUser = data.get("matchedUser");

    final var allQuestionsCount = data.get("allQuestionsCount");
    final var submitStats = matchedUser.get("submitStats");
    final var acSubmissions = submitStats.get("acSubmissionNum");
    final var contributions = matchedUser.get("contributions");
    final var profile = matchedUser.get("profile");

    final int totalQuestions = getCountByDifficulty(allQuestionsCount, "All");
    final int totalEasy = getCountByDifficulty(allQuestionsCount, "Easy");
    final int totalMedium = getCountByDifficulty(allQuestionsCount, "Medium");
    final int totalHard = getCountByDifficulty(allQuestionsCount, "Hard");

    final int solvedTotal = getCountByDifficulty(acSubmissions, "All");
    final int solvedEasy = getCountByDifficulty(acSubmissions, "Easy");
    final int solvedMedium = getCountByDifficulty(acSubmissions, "Medium");
    final int solvedHard = getCountByDifficulty(acSubmissions, "Hard");

    final int totalSubmissions = getCountByDifficulty(submitStats.get("totalSubmissionNum"), "All");

    return UserStatsResponse.builder()
        .username(username)
        .problemsSolved(UserStatsResponse.ProblemsSolved.builder()
            .totalQuestions(totalQuestions)
            .totalSolved(solvedTotal)
            .easySolved(solvedEasy)
            .totalEasy(totalEasy)
            .mediumSolved(solvedMedium)
            .totalMedium(totalMedium)
            .hardSolved(solvedHard)
            .totalHard(totalHard)
            .acceptanceRate(calculateRate(solvedTotal, totalSubmissions))
            .build())
        .contributionStats(UserStatsResponse.ContributionStats.builder()
            .points(contributions.get("points").asInt())
            .reputation(profile.get("reputation").asInt())
            .globalRanking(profile.get("ranking").asInt())
            .build())
        .build();
  }

  private int getCountByDifficulty(final JsonNode jsonArray, final String difficulty) {
    for (final JsonNode node : jsonArray) {
      if (node.get("difficulty").asText().equals(difficulty)) {
        return node.get("count").asInt();
      }
    }
    return 0;
  }

  private BigDecimal calculateRate(final int numerator, final int denominator) {
    if (denominator == 0) {
      return BigDecimal.ZERO;
    }
    return BigDecimal.valueOf(numerator)
        .multiply(BigDecimal.valueOf(100))
        .divide(BigDecimal.valueOf(denominator), 2, RoundingMode.HALF_UP);
  }
}
