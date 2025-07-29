package kg.attractor.bookingsaas.exceptions;

import lombok.Getter;

@Getter
public class AlreadyExistsException extends RuntimeException {
    private final String messageCode;

    public AlreadyExistsException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
    }
}