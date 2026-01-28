package eu.martin.store.checkout;

public class CartException extends RuntimeException {
    CartException(String message) {
        super(message);
    }
}
