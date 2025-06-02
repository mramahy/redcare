package com.redcare.rest.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotInFutureValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotInFuture {
    String message() default "creation date must not be in the future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
