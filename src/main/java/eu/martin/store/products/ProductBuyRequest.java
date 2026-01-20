package eu.martin.store.products;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductBuyRequest(
        @NotNull @Min(1) Short quantity,
        @NotNull BigDecimal buyPrice,
        String supplier
) {
}
