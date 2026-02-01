package eu.martin.store.products;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "categories")
class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Setter
    @Column(nullable = false, unique = true)
    private String name;

    @Setter
    private String description;

    @Setter
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    static Category of(String name) {
        var category = new Category();
        category.setName(name);
        return category;
    }
}
