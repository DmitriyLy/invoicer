package io.dmly.invoicer.exception;

public class TokenIsExpiredException extends RuntimeException {

    public TokenIsExpiredException(String message) {
        super(message);
    }

    public TokenIsExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
