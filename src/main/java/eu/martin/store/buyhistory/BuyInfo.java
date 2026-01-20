package eu.martin.store.buyhistory;

import eu.martin.store.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Table(name = "buy_info")
public class BuyInfo {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Setter
    private String supplier;
    @Setter
    private Float buyPrice;
    @Setter
    @ManyToOne(cascade = MERGE)
    private Product product;
}
