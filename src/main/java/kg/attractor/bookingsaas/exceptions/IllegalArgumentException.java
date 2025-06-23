package kg.attractor.bookingsaas.exceptions;

public class IllegalArgumentException extends RuntimeException {
    private final String messageCode;
    private final Object[] args;


    public IllegalArgumentException(String messageCode, Object[] args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = args;
    }

    public IllegalArgumentException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = null;
    }
}