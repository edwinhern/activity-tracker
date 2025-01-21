package com.activity.model.entity.leetcode;

import java.util.ArrayList;
import java.util.List;

import com.activity.model.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", schema = "leetcode")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LeetCodeUser extends BaseEntity {
  @Column(name = "username", unique = true, nullable = false)
  private String username;

  @Column(name = "name")
  private String name;

  @Column(name = "avatar")
  private String avatar;

  @Column(name = "country")
  private String country;

  @Column(name = "school")
  private String school;

  @Column(name = "github")
  private String github;

  @Column(name = "birthday")
  private String birthday;

  @JsonManagedReference
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderBy("createdAt DESC")
  @Builder.Default
  private List<TotalProblems> totalProblems = new ArrayList<>();

  @JsonManagedReference
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderBy("createdAt DESC")
  @Builder.Default
  private List<TotalSolved> totalSolved = new ArrayList<>();

  @JsonManagedReference
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderBy("createdAt DESC")
  @Builder.Default
  private List<SubmissionStats> submissionStats = new ArrayList<>();
}
