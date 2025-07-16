package kg.attractor.bookingsaas.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kg.attractor.bookingsaas.annotations.validators.DurationAtLeastFiveMinutesValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DurationAtLeastFiveMinutesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DurationAtLeastFiveMinutes {
    String message() default "The duration between start and end should be at least 5 minutes";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
