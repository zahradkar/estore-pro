package eu.martin.store.orders;

import eu.martin.store.products.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "order_items")
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Setter
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Setter
    private BigDecimal unitPrice;

    @Setter
    private Integer quantity;

    @Setter
    private BigDecimal totalPrice;

    OrderItem(Order order, Product product, Integer quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getSellPrice();
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
