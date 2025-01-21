package com.activity.config.leetcode;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import reactor.netty.http.client.HttpClient;

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
        .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, client.getConnectTimeout())
            .responseTimeout(Duration.ofMillis(client.getReadTimeout()))))
        .build();
  }
}
