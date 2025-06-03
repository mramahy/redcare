package com.redcare.error;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;

/**
 * Custom exception for GitHub-related errors, including HTTP status and error message for REST
 * responses.
 */
public class GithubException extends RuntimeException {

  private final HttpStatus status;
  private final String errorMessage;

  /**
   * Constructs a GithubException with a specific HTTP status and error message.
   *
   * @param status       the HTTP status to return in the REST response
   * @param errorMessage the error message to include in the REST response
   */
  public GithubException(HttpStatus status, String errorMessage) {
    super(errorMessage);
    this.status = status;
    this.errorMessage = errorMessage;
  }

  /**
   * Constructs a GithubException with a default status of BAD_REQUEST.
   *
   * @param errorMessage the error message to include in the REST response
   */
  public GithubException(String errorMessage) {
    this(HttpStatus.BAD_REQUEST, errorMessage);
  }

  /**
   * Returns the HTTP status associated with this exception.
   *
   * @return the HTTP status
   */
  public HttpStatus getStatus() {
    return status;
  }

  /**
   * Converts the exception details into a response body map.
   *
   * @return a map containing the timestamp, status code, reason phrase, and error message
   */
  public Map<String, String> toResponseBody() {
    return Map.of(
        "timestamp", Instant.now().toString(),
        "status", String.valueOf(status.value()),
        "error", status.getReasonPhrase(),
        "message", errorMessage
    );
  }
}