package com.activity.model.dto.leetcode;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatsResponse {
  private String username;
  private ProblemsSolved problemsSolved;
  private SubmissionStats submissionStats;
  private ContributionStats contributionStats;

  @Data
  @Builder
  public static class ProblemsSolved {
    private int totalSolved;
    private int totalQuestions;
    private int easySolved;
    private int totalEasy;
    private int mediumSolved;
    private int totalMedium;
    private int hardSolved;
    private int totalHard;
    private BigDecimal acceptanceRate;
  }

  @Data
  @Builder
  public static class SubmissionStats {
    private int lastSevenDays;
    private int lastThirtyDays;
    private int lastYear;
    private double averageRuntime;
    private double averageMemory;
  }

  @Data
  @Builder
  public static class ContributionStats {
    private int points;
    private int reputation;
    private int globalRanking;
  }
}
