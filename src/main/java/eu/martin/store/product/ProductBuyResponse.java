package eu.martin.store.product;

import java.math.BigDecimal;

record ProductBuyResponse(
        int id,
        String name,
        short newQuantity,
        BigDecimal lastBuyPrice,
        String lastSupplier
) {
}
