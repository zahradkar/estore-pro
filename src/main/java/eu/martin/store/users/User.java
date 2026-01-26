package eu.martin.store.users;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Setter
    private String name;

    @Setter
    private String email;

    @Setter
    private String password;

    @Setter
    @Enumerated(EnumType.STRING)
    private Role role;

    @Setter
    @Column(name = "verified", nullable = false)
    private boolean verified;

    @Column(name = "registered_at", nullable = false, insertable = false, updatable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP")
    @CreatedDate
    private LocalDateTime registeredAt;

    @OneToMany(cascade = REMOVE)
    @JoinColumn(name = "user_id")
    private Set<Address> addresses = new HashSet<>();

    void addAddress(Address address) {
        addresses.add(address);
    }
}
