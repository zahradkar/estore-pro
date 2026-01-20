package eu.martin.store.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductBuyRequest(
        @NotNull @Min(1) Short quantity,
        @NotNull Float buyPrice,
        String supplier
) {
}
