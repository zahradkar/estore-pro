package eu.martin.store.cart;

public class ItemNotFoundException extends RuntimeException {
    ItemNotFoundException() {
        super("Item not found!");
    }
}
