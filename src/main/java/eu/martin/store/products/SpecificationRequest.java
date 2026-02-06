package eu.martin.store.products;

import java.math.BigDecimal;

record SpecificationRequest(
        Short pageNumber,
        Short pageSize,
        String name,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        String attributeName,
        String categoryName,
        String productProperty,
        Boolean descending
) {
}
