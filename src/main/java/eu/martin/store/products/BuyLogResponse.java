package eu.martin.store.products;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

record BuyLogResponse(
        ProductSummary product,
        List<BuyLogDto> logs
) {
    record ProductSummary(
            String name,
            BigDecimal sellPrice,
            int quantity,
            MeasureUnit measureUnit
    ) {
    }

    record BuyLogDto(
            long id,
            BigDecimal buyPrice,
            String supplier,
            LocalDateTime timestamp
    ) {
    }
}
