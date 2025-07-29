package kg.attractor.bookingsaas.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends RuntimeException{
    private final String messageCode;

    public NotFoundException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
    }
}
