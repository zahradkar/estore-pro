package eu.martin.store.products;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@Entity
@Table(name = "product_attributes")
class Attribute {
    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DataType type;

    @Setter
    @Column(nullable = false)
    private Boolean mandatory;

    @EqualsAndHashCode.Exclude
    @Setter
    @Column(name = "val") // note: "value" is keyword in SQL
    private String value;

    @AllArgsConstructor
    enum DataType {
        STRING("text"),
        BYTE("celé číslo od -128 do +127"),
        SHORT("celé číslo od -32 768 do +32 767"),
        INTEGER("celé číslo od -2 147 483 648 do +2 147 483 647"),
        DOUBLE("desatinné číslo"),
        LOCALDATETIME("dátum a čas"),
        DATE("dátum"),
        TIME("čas"),
        BOOLEAN("áno/nie");

        final String name;
    }
}
