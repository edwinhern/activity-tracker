package com.activity.model.entity.leetcode;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users", schema = "leetcode")
@Data
public class LeetCodeUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "leetcode_username", unique = true, nullable = false)
  private String username;

  private Integer totalSolved;
  private Integer totalQuestions;
  private Integer totalEasy;
  private Integer totalMedium;
  private Integer totalHard;
  private Integer easySolved;
  private Integer mediumSolved;
  private Integer hardSolved;
  private BigDecimal acceptanceRate;
  private Integer contributionPoints;
  private Integer reputation;
  private Integer globalRanking;

  @Column(name = "created_at")
  private ZonedDateTime createdAt;

  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = updatedAt = ZonedDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = ZonedDateTime.now();
  }
}
