package ru.troyanov.transcribtionservice.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FileFormatValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFileFormatOrEmpty {
    String message() default "Format not supported or is empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] allowedFormats() default {"wav", "mp3", "wave", "ogg", "mp4"};
}
