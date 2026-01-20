package eu.martin.store.products;

import java.math.BigDecimal;

record ProductBuyResponse(
        int id,
        String name,
        short newQuantity,
        BigDecimal lastBuyPrice,
        String lastSupplier
) {
}
