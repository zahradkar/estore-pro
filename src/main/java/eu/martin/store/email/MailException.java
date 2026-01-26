package eu.martin.store.email;

public class MailException extends RuntimeException {
    public MailException(String message) {
        super(message);
    }
}
