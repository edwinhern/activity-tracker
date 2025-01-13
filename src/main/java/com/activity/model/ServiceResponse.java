package com.activity.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ServiceResponse<T> {
  private Boolean success;
  private String message;
  private T data;
  private int statusCode;

  // Success response builders
  public static <T> ServiceResponse<T> success(final String message, final T data) {
    return success(message, data, HttpStatus.OK);
  }

  public static <T> ServiceResponse<T> success(final String message, final HttpStatusCode statusCode) {
    return success(message, null, statusCode);
  }

  public static <T> ServiceResponse<T> success(final String message, final T data, final HttpStatusCode statusCode) {
    return ServiceResponse.<T>builder()
        .success(true)
        .message(message)
        .data(data)
        .statusCode(statusCode.value())
        .build();
  }

  // Failure response builders
  public static <T> ServiceResponse<T> failure(final String message) {
    return failure(message, null, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public static <T> ServiceResponse<T> failure(final String message, final HttpStatusCode statusCode) {
    return failure(message, null, statusCode);
  }

  public static <T> ServiceResponse<T> failure(final String message, final T data, final HttpStatusCode statusCode) {
    return ServiceResponse.<T>builder()
        .success(false)
        .message(message)
        .data(data)
        .statusCode(statusCode.value())
        .build();
  }
}
