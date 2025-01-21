package com.activity.model.dto.leetcode;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeetCodeUserDTO {
  private Long id;
  private String username;
  private String name;
  private String avatar;
  private String country;
  private String school;
  private String github;
  private String birthday;
  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;
}
