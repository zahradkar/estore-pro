package eu.martin.store.cart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

record UpdateCartItemRequest(@NotNull @Min(1) @Max(1000) Short quantity) {
}
