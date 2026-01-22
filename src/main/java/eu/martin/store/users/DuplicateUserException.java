package eu.martin.store.users;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException() {
        super("error: e-mail already registered!");
    }
}
