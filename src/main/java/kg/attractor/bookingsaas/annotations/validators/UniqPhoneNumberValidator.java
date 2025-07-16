package kg.attractor.bookingsaas.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.bookingsaas.annotations.UniqueUserPhoneNumber;
import kg.attractor.bookingsaas.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniqPhoneNumberValidator implements ConstraintValidator<UniqueUserPhoneNumber, String> {
    private final UserService userService;

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return userService.isUserPhoneNumberUnique(phoneNumber);
    }
}
