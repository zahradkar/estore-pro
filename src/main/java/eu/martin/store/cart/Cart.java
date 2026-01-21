package eu.martin.store.cart;

import eu.martin.store.products.Product;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date_created", insertable = false, updatable = false, columnDefinition = "DATE DEFAULT (CURRENT_DATE)")
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<CartItem> items = new LinkedHashSet<>();

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    CartItem getItem(Integer productId) {
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    CartItem addItem(Product product, short quantity) {
        var cartItem = getItem(product.getId());
        if (cartItem == null) {
            if (quantity > product.getQuantity())
                throw new QuantityExceedException();

            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity((int) quantity);
            cartItem.setCart(this);
            items.add(cartItem);
        } else {
            if (cartItem.getQuantity() + quantity > product.getQuantity())
                throw new QuantityExceedException();

            cartItem.addQuantity(quantity);
        }

        return cartItem;
    }

    void removeItem(Integer productId) {
        var cartItem = getItem(productId);
        if (cartItem != null) {
            items.remove(cartItem);
            cartItem.setCart(null);
        }
    }

    void clear() {
        items.clear();
    }
}
