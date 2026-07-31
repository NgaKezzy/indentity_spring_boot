package com.mysql.DEMO_MYSQL.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {
        DobValidator.class
})
public @interface DobConstraints {

    String message() default "DOB_UNDER_AGE";

    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
