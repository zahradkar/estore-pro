package eu.martin.store.product;

import jakarta.validation.constraints.NotBlank;

record ProductRegisterDto(
        @NotBlank String name,
        String description,
        Float sellPrice,
        Float quantity,
        MeasureUnit measureUnit) {
}
