package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class NotBefore1895Validator implements ConstraintValidator<NotBefore1895, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        return value.isAfter(minDate) || value.isEqual(minDate);
    }
}


