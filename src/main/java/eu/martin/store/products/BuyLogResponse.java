package eu.martin.store.products;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

record BuyLogResponse(
        Product product,
        List<BuyLog> logs
) {
    record BuyLog(
            long id,
            BigDecimal buyPrice,
            String supplier,
            LocalDateTime timestamp) {
    }
}
