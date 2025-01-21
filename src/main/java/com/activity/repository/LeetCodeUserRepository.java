package com.activity.repository;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.activity.model.entity.leetcode.LeetCodeUser;

@Repository
public interface LeetCodeUserRepository extends JpaRepository<LeetCodeUser, Long> {
  Optional<LeetCodeUser> findByUsername(String username);

  @Query("""
          SELECT u FROM LeetCodeUser u
          LEFT JOIN FETCH u.totalProblems
          LEFT JOIN FETCH u.totalSolved
          LEFT JOIN FETCH u.submissionStats
          WHERE u.username = :username
      """)
  Optional<LeetCodeUser> findByUsernameWithStats(String username);

  @Query("""
          SELECT u FROM LeetCodeUser u
          LEFT JOIN FETCH u.totalProblems tp
          LEFT JOIN FETCH u.totalSolved ts
          LEFT JOIN FETCH u.submissionStats ss
          WHERE u.username = :username
          AND (tp.createdAt BETWEEN :startDate AND :endDate
               OR ts.createdAt BETWEEN :startDate AND :endDate
               OR ss.createdAt BETWEEN :startDate AND :endDate)
      """)
  Optional<LeetCodeUser> findByUsernameWithStatsInDateRange(
      String username,
      ZonedDateTime startDate,
      ZonedDateTime endDate);
}
