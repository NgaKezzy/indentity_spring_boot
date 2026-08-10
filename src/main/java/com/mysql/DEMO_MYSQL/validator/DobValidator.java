package com.mysql.DEMO_MYSQL.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DobValidator implements ConstraintValidator<DobConstraints, LocalDate> {
  private int min;

  @Override
  public void initialize(DobConstraints constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    min = constraintAnnotation.min();
  }

  @Override
  public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
    if (value == null) {
      return true; // Consider null as valid, use @NotNull for null checks
    }
    LocalDate today = LocalDate.now();
    LocalDate minDate = today.minusYears(min);
    // dob phải <= (hôm nay - min năm) → người dùng phải đủ `min` tuổi
    // VD: min=18, today=2026-07-31 → minDate=2008-07-31
    //     dob=2019 → 2019 AFTER 2008 → chưa đủ 18 tuổi → false (invalid) ✅
    //     dob=2000 → 2000 NOT AFTER 2008 → đủ 18 tuổi → true (valid)   ✅
    return !value.isAfter(minDate);
  }
}
