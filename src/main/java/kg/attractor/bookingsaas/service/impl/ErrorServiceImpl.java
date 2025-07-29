package kg.attractor.bookingsaas.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import kg.attractor.bookingsaas.exceptions.body.ValidationErrorBody;
import kg.attractor.bookingsaas.exceptions.body.ValidationExceptionBody;
import kg.attractor.bookingsaas.service.ErrorService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ErrorServiceImpl implements ErrorService {

    @Override
    public ValidationExceptionBody handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest httpServletRequest
    ) {
        List<ValidationErrorBody> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {

            if (error instanceof FieldError fieldError)
                getFieldErrors(fieldError, errors);
        });

        return ValidationExceptionBody.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(httpServletRequest.getMethod())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .cause("Validation Error")
                .message("Exception " + ex.getClass().getSimpleName() + "is happened, check errors field for more details")
                .exception(ex.getClass().getSimpleName())
                .errors(errors)
                .path(httpServletRequest.getRequestURL().toString())
                .build();
    }

    private void getFieldErrors(FieldError fieldError, List<ValidationErrorBody> errors) {
        String message = fieldError.getDefaultMessage();
        String objectName = fieldError.getObjectName();
        String fieldName = fieldError.getField();
        Object rejectedValue = fieldError.getRejectedValue();

        errors.add(ValidationErrorBody
                .builder()
                .objectName(objectName)
                .rejectedValue(rejectedValue)
                .fieldName(fieldName)
                .message(message)
                .build());
    }

    @Override
    public Map<String, Object> handleMethodValidationException(
            HandlerMethodValidationException ex, HttpServletRequest request
    ) {
        Map<String, Object> errors = new HashMap<>();

        ex.getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            Object arguments = error.getArguments();

            errors.put("timestamp", DateTimeFormatter
                    .ofPattern("dd:MM:yyyy HH:mm:ss")
                    .format(LocalDateTime.now()));

            errors.put("message", errorMessage);
            errors.put("arguments", arguments);
            errors.put("path", request.getRequestURI());
        });

        return errors;
    }

    @Override
    public Map<String, Object> handleDateTimeParserException(DateTimeParseException ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm:ss").format(LocalDateTime.now()));
        map.put("status", HttpStatus.BAD_REQUEST.value());
        map.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        map.put("message", ex.getMessage());
        map.put("rejected value", ex.getParsedString());
        map.put("correct format", "yyyy:MM:dd HH:mm:ss");
        return map;
    }
}
