package eu.martin.store.orders;

import eu.martin.store.cart.Cart;
import eu.martin.store.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Setter
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Setter
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = {PERSIST, REMOVE})
    private Set<OrderItem> items = new LinkedHashSet<>();

    public static Order fromCart(Cart cart, User customer) {
        var order = new Order();
        order.customer = customer;
        order.status = PaymentStatus.PENDING;
        order.totalPrice = cart.getTotalPrice();

        cart.getItems().forEach(item -> order.items.add(new OrderItem(order, item.getProduct(), item.getQuantity())));

        return order;
    }

    public boolean isPlacedBy(User customer) {
        return this.customer.equals(customer);
    }
}
