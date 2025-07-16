package kg.attractor.bookingsaas.service;

import jakarta.servlet.http.HttpServletRequest;
import kg.attractor.bookingsaas.exceptions.body.ValidationExceptionBody;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.format.DateTimeParseException;
import java.util.Map;

public interface ErrorService {
    ValidationExceptionBody handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest httpServletRequest);

    Map<String, Object> handleMethodValidationException(HandlerMethodValidationException ex, HttpServletRequest request);

    Map<String, Object> handleDateTimeParserException(DateTimeParseException ex);
}
