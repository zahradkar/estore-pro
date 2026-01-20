package eu.martin.store.products;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

record ProductRegisterDto(
        @NotBlank String name,
        String description,
        BigDecimal sellPrice,
        MeasureUnit measureUnit) {
}
