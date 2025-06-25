package kg.attractor.bookingsaas.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kg.attractor.bookingsaas.annotations.validators.DurationBookTimesValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = DurationBookTimesValidator.class)
public @interface DurationBookTimes {
    long durationInSeconds() default 600;
    String message() default "Duration is not correct";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
