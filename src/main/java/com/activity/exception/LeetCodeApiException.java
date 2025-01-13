package com.activity.exception;

public class LeetCodeApiException extends RuntimeException {
  public LeetCodeApiException(final String message) {
    super(message);
  }

  public LeetCodeApiException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
