package ua.conference.exception;

public class DuplicateIdentityException extends IllegalStateException {
    public DuplicateIdentityException(String message) {
        super(message);
    }
}