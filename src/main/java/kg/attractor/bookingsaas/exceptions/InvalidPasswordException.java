package kg.attractor.bookingsaas.exceptions;

import lombok.Getter;

@Getter
public class InvalidPasswordException extends RuntimeException {
    private final String messageCode;
    private final Object[] args;

    public InvalidPasswordException(String messageCode, Object[] args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = args;
    }
    public InvalidPasswordException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = null;
    }
}