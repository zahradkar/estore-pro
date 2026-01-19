package eu.martin.store.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Setter
    private String name;
    @Setter
    private String description;
    @Setter
    private Float sellPrice;
    @Setter
    private Float quantity;
    @Setter
    private MeasureUnit measureUnit;
}
