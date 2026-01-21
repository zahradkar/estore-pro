package eu.martin.store.cart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

record CartItemRequest(
        @NotNull @Min(1) Integer productId,
        @NotNull @Min(1) @Max(30_000) Short quantity
) {
}
