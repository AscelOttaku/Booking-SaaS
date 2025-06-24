package kg.attractor.bookingsaas.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kg.attractor.bookingsaas.annotations.validators.UniqueEmailValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueUserEmail {
    String message() default "email should be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
