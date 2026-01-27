package eu.martin.store.products;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.GenerationType.IDENTITY;
@ToString
@Getter
@Entity
@Table(name = "buy_logs")
class BuyLog {
    @ToString.Exclude
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Setter
    private String supplier;

    @Setter
    private BigDecimal buyPrice;

    @Setter
    @CreationTimestamp
    @Column(insertable = false, updatable = false)
    private LocalDateTime timestamp;

    @Setter
    @ManyToOne(cascade = MERGE)
    private Product product;
}
