package com.activity.service.leetcode;

import com.activity.model.dto.leetcode.UserStatsResponse;

public interface LeetCodeApiClient {
  UserStatsResponse fetchUserStats(String username);
}
