package eu.martin.store.users;

class UserAlreadyVerifiedException extends RuntimeException {
    public UserAlreadyVerifiedException() {
        super("User already verified!");
    }
}
