package com.activity.model.entity.leetcode;

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
@Table(name = "total_solved", schema = "leetcode")
@Data
@EqualsAndHashCode(callSuper = true)
public class TotalSolved extends BaseEntity {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private LeetCodeUser user;

  @Column(name = "total_count")
  private Integer totalCount;

  @Column(name = "easy_count")
  private Integer easyCount;

  @Column(name = "medium_count")
  private Integer mediumCount;

  @Column(name = "hard_count")
  private Integer hardCount;
}
