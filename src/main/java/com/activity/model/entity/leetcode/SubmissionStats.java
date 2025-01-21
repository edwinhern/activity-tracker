package com.activity.model.entity.leetcode;

import java.math.BigDecimal;

import com.activity.model.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "submission_stats", schema = "leetcode")
@Data
@EqualsAndHashCode(callSuper = true)
public class SubmissionStats extends BaseEntity {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private LeetCodeUser user;

  @Column(name = "points")
  private Integer points;

  @Column(name = "reputation")
  private Integer reputation;

  @Column(name = "global_ranking")
  private Integer globalRanking;

  @Column(name = "acceptance_rate")
  private BigDecimal acceptanceRate;
}
