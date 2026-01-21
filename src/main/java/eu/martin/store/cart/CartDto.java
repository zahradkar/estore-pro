package eu.martin.store.cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

record CartDto(
        UUID id,
        List<CartItemResponse> items,
        BigDecimal totalPrice
) {
}
