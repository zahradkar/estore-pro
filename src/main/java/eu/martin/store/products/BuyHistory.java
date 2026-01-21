package eu.martin.store.products;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
class BuyHistory {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Setter
    private String supplier;
    @Setter
    private BigDecimal buyPrice;
    @Setter
    @ManyToOne(cascade = MERGE)
    private Product product;
}
