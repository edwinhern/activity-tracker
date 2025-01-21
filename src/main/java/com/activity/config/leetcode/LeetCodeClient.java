package com.activity.config.leetcode;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.activity.config.leetcode.mapper.LeetCodeResponseMapper;
import com.activity.config.leetcode.query.LeetCodeQueries;
import com.activity.exception.LeetCodeApiException;
import com.activity.model.entity.leetcode.LeetCodeUser;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LeetCodeClient {
  private final WebClient leetCodeWebClient;
  private final LeetCodeResponseMapper leetCodeResponseMapper;
  private final LeetCodeConfig leetCodeConfig;

  @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
  public LeetCodeUser fetchUserStats(final String username) {
    log.info("Fetching stats for user: {}", username);

    final var response = executeGraphQLQuery(LeetCodeQueries.USER_PROFILE,
        Map.of("username", username));

    if (response == null || !response.has("data")) {
      throw new LeetCodeApiException("Failed to fetch user stats");
    }

    return leetCodeResponseMapper.toUserStatsResponse(username, response);
  }

  private JsonNode executeGraphQLQuery(final String query, final Map<String, Object> variables) {
    return leetCodeWebClient.post()
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(Map.of(
            "query", query,
            "variables", variables))
        .retrieve()
        .bodyToMono(JsonNode.class)
        .timeout(Duration.ofMillis(leetCodeConfig.getClient().getReadTimeout()))
        .doOnError(error -> log.error("GraphQL query failed", error))
        .block();
  }
}
