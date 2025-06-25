package kg.attractor.bookingsaas.exceptions.body;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ValidationErrorBody {
    private String objectName;
    private Object rejectedValue;
    private String fieldName;
    private String message;
}
