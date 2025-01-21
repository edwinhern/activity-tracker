package com.activity.config.leetcode.query;

public final class LeetCodeQueries {
    private LeetCodeQueries() {
        // Private constructor to prevent instantiation
    }

    public static final String USER_PROFILE = """
                query getUserProfile($username: String!) {
                    allQuestionsCount {
                        difficulty
                        count
                    }
                    matchedUser(username: $username) {
                        username
                        githubUrl
                        contributions {
                            points
                        }
                        profile {
                            realName
                            userAvatar
                            birthday
                            ranking
                            reputation
                            websites
                            countryName
                            company
                            school
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

}
