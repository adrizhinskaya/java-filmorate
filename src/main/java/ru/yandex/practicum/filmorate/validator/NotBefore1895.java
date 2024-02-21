package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBefore1895Validator.class)
public @interface NotBefore1895 {
    String message() default "NotBefore1895.invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}