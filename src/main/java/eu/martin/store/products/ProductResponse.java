package eu.martin.store.products;

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
