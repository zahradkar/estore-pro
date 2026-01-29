package eu.martin.store.products;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

@Getter
@Entity
@Table(name = "products")
public class Product {
    @OneToMany(cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private final Set<Attribute> attributes = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    private String description;

    @Setter
    private BigDecimal sellPrice;

    @Setter
    @ColumnDefault("0")
    private Integer quantity;

    @Setter
    @Column(nullable = false)
    private MeasureUnit measureUnit;

    void increaseQuantity(short quantity) {
        this.quantity += quantity;
    }
}
