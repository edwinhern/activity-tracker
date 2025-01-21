package com.activity.config.leetcode;

import com.activity.model.entity.leetcode.LeetCodeUser;

public interface ILeetCodeClient {
  LeetCodeUser fetchUserStats(String username);
}
