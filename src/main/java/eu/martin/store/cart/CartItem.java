package eu.martin.store.cart;

import eu.martin.store.products.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @Setter
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @Setter
    private Product product;

    @Setter
    private Integer quantity;

    BigDecimal getTotalPrice() {
        return product.getSellPrice().multiply(BigDecimal.valueOf(quantity));
    }

    void addQuantity(short quantity) {
        this.quantity += quantity;
    }
}
