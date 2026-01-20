package eu.martin.store.product;

import java.math.BigDecimal;

record ProductResponse(
        int id,
        String name,
        String description,
        BigDecimal sellPrice,
        short quantity,
        MeasureUnit measureUnit
) {
}
