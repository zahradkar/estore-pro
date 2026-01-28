package eu.martin.store.checkout;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

record CheckoutRequest(@NotNull UUID cartId) {
}
