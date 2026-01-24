package eu.martin.store.products;

import java.math.BigDecimal;
import java.time.LocalDateTime;

record ProductBuyResponse(
        int id,
        String name,
        short newQuantity,
        BigDecimal lastBuyPrice,
        String lastSupplier,
        LocalDateTime timestamp
) {
}
