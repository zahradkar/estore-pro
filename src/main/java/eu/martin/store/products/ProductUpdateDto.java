package eu.martin.store.products;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

record ProductUpdateDto(
        @NotBlank String name,
        String description,
        @NotNull BigDecimal sellPrice,
        @NotNull @Max(30_000) Short quantity,
        @NotNull MeasureUnit measureUnit
) {
}
