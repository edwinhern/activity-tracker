package com.activity.config.leetcode;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "leetcode")
@Getter
@Setter
public class LeetCodeConfig {
  private ApiConfig api;
  private ClientConfig client;

  @Data
  public static class ApiConfig {
    private String baseUrl;
    private String username;
    private String contentType;
  }

  @Data
  public static class ClientConfig {
    private int connectTimeout;
    private int readTimeout;
    private RetryConfig retry;
  }

  @Data
  public static class RetryConfig {
    private int maxAttempts;
    private int delay;
  }

  @Bean
  public WebClient leetCodeWebClient() {
    return WebClient.builder()
        .baseUrl(api.getBaseUrl())
        .defaultHeader("Referer", "https://leetcode.com/%s".formatted(api.getUsername()))
        .defaultHeader("Content-Type", api.getContentType())
        .build();
  }
}
