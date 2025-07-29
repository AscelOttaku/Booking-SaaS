package kg.attractor.bookingsaas.exceptions;

import lombok.Getter;

@Getter
public class BadCredentialsException extends RuntimeException{
    private final String messageCode;

    public BadCredentialsException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
    }
}