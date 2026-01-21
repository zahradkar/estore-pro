package eu.martin.store.products;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "products")
public class Product {
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
    private short quantity;
    @Setter
    @Column(nullable = false)
    private MeasureUnit measureUnit;

    void increaseQuantity(short quantity) {
        this.quantity += quantity;
    }
}
