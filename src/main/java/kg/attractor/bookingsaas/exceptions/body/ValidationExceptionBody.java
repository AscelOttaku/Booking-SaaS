package kg.attractor.bookingsaas.exceptions.body;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
@Getter
public class ValidationExceptionBody {
    private final String timestamp = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .format(LocalDateTime.now());

    private int statusCode;
    private String method;
    private String error;
    private String cause;
    private String message;
    private String exception;
    private List<ValidationErrorBody> errors;
    private String path;
}
