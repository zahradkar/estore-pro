package eu.martin.store.products;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.*;

@Getter
@Entity
@Table(name = "products")
public class Product {
    @OneToMany(cascade = {PERSIST, MERGE, REMOVE}, orphanRemoval = true)
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
    @Column(nullable = false, columnDefinition = "varchar(10)")
    @Enumerated(EnumType.STRING)
    private MeasureUnit measureUnit;

    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    void increaseQuantity(short quantity) {
        this.quantity += quantity;
    }

    void setCategories(Set<Category> categories) {
        this.categories = new HashSet<>(categories);
    }

    void removeCategory(Category category) {
        this.categories.remove(category);
    }
}
