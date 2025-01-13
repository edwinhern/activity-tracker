package com.activity.repository.leetcode;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.activity.model.entity.leetcode.LeetCodeUser;

@Repository
public interface LeetCodeUserRepository extends JpaRepository<LeetCodeUser, Long> {
  @Query(value = "SELECT * FROM leetcode.users WHERE leetcode_username = :username", nativeQuery = true)
  Optional<LeetCodeUser> findByUsername(String username);

  @Query(value = """
      SELECT * FROM leetcode.users
      WHERE updated_at < :staleThreshold
      ORDER BY updated_at ASC
      LIMIT :batchSize
      """, nativeQuery = true)
  List<LeetCodeUser> findStaleUsers(ZonedDateTime staleThreshold, int batchSize);
}
