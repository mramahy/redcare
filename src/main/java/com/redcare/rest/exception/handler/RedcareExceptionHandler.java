package com.redcare.rest.exception.handler;

import com.redcare.error.GithubException;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * RedcareExceptionHandler is a global exception handler for REST controllers. It handles specific
 * exceptions and provides appropriate HTTP responses.
 */
@RestControllerAdvice
public class RedcareExceptionHandler {

  /**
   * Handles validation exceptions caused by invalid method arguments.
   *
   * @param ex the MethodArgumentNotValidException containing validation errors
   * @return a ResponseEntity containing a map of field errors and a BAD_REQUEST status
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationException(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = mapFieldErrors(ex);
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles GithubException and provides a response with the appropriate status and error details.
   *
   * @param ex the GithubException containing error details
   * @return a ResponseEntity containing the error response body and the associated HTTP status
   */
  @ExceptionHandler(GithubException.class)
  public ResponseEntity<Map<String, String>> handleValidationException(GithubException ex) {
    return new ResponseEntity<>(ex.toResponseBody(), ex.getStatus());
  }

  /**
   * Maps field errors from a MethodArgumentNotValidException to a key-value pair.
   *
   * @param ex the MethodArgumentNotValidException containing field errors
   * @return a map where the key is the field name and the value is the error message
   */
  private Map<String, String> mapFieldErrors(MethodArgumentNotValidException ex) {
    return ex.getBindingResult().getFieldErrors()
        .stream()
        .filter(e -> e.getDefaultMessage() != null)
        .collect(Collectors.toMap(
            FieldError::getField,
            DefaultMessageSourceResolvable::getDefaultMessage,
            (existingValue, newValue) -> existingValue));
  }
}