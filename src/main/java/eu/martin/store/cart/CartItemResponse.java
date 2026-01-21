package eu.martin.store.cart;

import java.math.BigDecimal;

record CartItemResponse(
        ProductDto product,
        int quantity,
        BigDecimal totalPrice) {
    record ProductDto(
            Integer id,
            String name,
            BigDecimal sellPrice) {
    }
}
