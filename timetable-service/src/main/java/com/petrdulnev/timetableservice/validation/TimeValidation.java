package com.petrdulnev.timetableservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TimeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeValidation {
    String message() default "Time not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
