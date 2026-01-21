package eu.martin.store.cart;

public class QuantityExceedException extends RuntimeException{
    QuantityExceedException() {
        super("Asked product quantity exceeded!");
    }
}
