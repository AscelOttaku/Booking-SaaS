package kg.attractor.bookingsaas.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kg.attractor.bookingsaas.annotations.validators.DurationAtLeastOneHourValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DurationAtLeastOneHourValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DurationAtLeastOneHour {
    String message() default "The duration between startTime and endTime must be at least 1 hour";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}