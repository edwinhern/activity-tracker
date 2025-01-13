package com.activity.service.leetcode.graphql;

public final class LeetCodeQueries {
  public static final String USER_PROFILE = """
          query getUserProfile($username: String!) {
              allQuestionsCount {
                  difficulty
                  count
              }
              matchedUser(username: $username) {
                  contributions {
                      points
                  }
                  profile {
                      reputation
                      ranking
                  }
                  submitStats {
                      acSubmissionNum {
                          difficulty
                          count
                          submissions
                      }
                      totalSubmissionNum {
                          difficulty
                          count
                          submissions
                      }
                  }
              }
          }
      """;

  public static final String RECENT_SUBMISSIONS = """
          query getRecentSubmissions($username: String!, $limit: Int) {
              recentSubmissionList(username: $username, limit: $limit) {
                  title
                  titleSlug
                  timestamp
                  statusDisplay
                  lang
              }
          }
      """;

  private LeetCodeQueries() {
    // Prevent instantiation
  }
}
