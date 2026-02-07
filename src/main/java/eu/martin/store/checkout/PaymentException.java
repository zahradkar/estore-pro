package eu.martin.store.checkout;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentException extends RuntimeException {
    PaymentException(String message) {
        super(message);
    }
}
