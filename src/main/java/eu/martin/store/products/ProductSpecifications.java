package eu.martin.store.products;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

record ProductSpecifications() {

    static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    static Specification<Product> hasPriceGreaterThanOrEqualTo(BigDecimal price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("sellPrice"), price);
    }

    static Specification<Product> hasPriceLessThanOrEqualTo(BigDecimal price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("sellPrice"), price);
    }

    static Specification<Product> hasAttribute(String attributeName) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, Attribute> attributeJoin = root.join("attributes");
            return criteriaBuilder.like(attributeJoin.get("name"), "%" + attributeName + "%");
        };
    }

    static Specification<Product> hasCategory(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, Category> categoryJoin = root.join("categories");
            return criteriaBuilder.like(categoryJoin.get("name"), "%" + categoryName + "%");
        };
    }
}
