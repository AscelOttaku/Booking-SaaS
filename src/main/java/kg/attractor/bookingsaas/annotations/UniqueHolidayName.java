package kg.attractor.bookingsaas.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kg.attractor.bookingsaas.annotations.validators.UniqueHolidayNameValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueHolidayNameValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueHolidayName {
    String message() default "Holiday name must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}