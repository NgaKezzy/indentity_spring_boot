package com.mysql.DEMO_MYSQL.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraints {

  String message() default "Date of Birth must be in the past";

  int min();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
