package eu.martin.store.orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

record OrderDto(
        long id,
        String status,
        LocalDateTime createdAt,
        List<OrderItemDto> items,
        BigDecimal totalPrice
) {
    record OrderItemDto(
            OrderProductDto product,
            int quantity,
            BigDecimal totalPrice
    ) {
        record OrderProductDto(
                Integer id,
                String name,
                BigDecimal sellPrice
        ) {
        }
    }
}
