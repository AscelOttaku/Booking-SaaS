package kg.attractor.bookingsaas.exceptions;

public class IllegalArgumentException extends RuntimeException {
    private final String messageCode;

    public IllegalArgumentException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
    }
}