package kg.attractor.bookingsaas.exceptions;

import lombok.Getter;

@Getter
public class InvalidPasswordException extends RuntimeException {
    private final String messageCode;

    public InvalidPasswordException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
    }
}