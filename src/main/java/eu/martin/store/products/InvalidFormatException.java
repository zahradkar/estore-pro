package eu.martin.store.products;

public class InvalidFormatException extends RuntimeException{
    InvalidFormatException(String message) {
        super(message);
    }
}
