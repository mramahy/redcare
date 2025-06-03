package com.redcare.rest.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Instant;

public class NotInFutureValidator implements ConstraintValidator<NotInFuture, Instant> {

  @Override
  public boolean isValid(Instant value, ConstraintValidatorContext context) {
    return value != null && !value.isAfter(Instant.now());
  }
}
