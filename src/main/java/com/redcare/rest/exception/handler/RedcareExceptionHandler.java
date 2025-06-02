package com.redcare.rest.exception.handler;

import com.redcare.error.GithubException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RedcareExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = mapFieldErrors(ex);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GithubException.class)
    public ResponseEntity<Map<String,String>> handleValidationException(GithubException ex) {
        return new ResponseEntity<>(ex.toResponseBody(), ex.getStatus());
    }

    private Map<String, String> mapFieldErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream().filter(e -> e.getDefaultMessage() != null)
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (existingValue, newValue) -> existingValue));
    }
}
