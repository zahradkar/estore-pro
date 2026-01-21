package eu.martin.store.orders;

import jakarta.persistence.*;

import java.util.*;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

//    private User custorem; // todo
//    private Set<OrderItem> items = new HashSet<>();
}
