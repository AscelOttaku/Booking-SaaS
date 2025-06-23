package kg.attractor.bookingsaas.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends RuntimeException{
    private final String messageCode;
    private final Object[] args;
    private HttpStatus status;

    public NotFoundException(String messageCode, Object[] args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = args;
    }
    public NotFoundException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = null;
    }

}
