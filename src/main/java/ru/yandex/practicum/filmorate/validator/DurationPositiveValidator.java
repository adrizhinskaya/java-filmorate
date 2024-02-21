package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;

public class DurationPositiveValidator implements ConstraintValidator<DurationPositive, Duration> {

    @Override
    public boolean isValid(Duration value, ConstraintValidatorContext context) {
        return value != null && !value.isNegative();
    }
}
