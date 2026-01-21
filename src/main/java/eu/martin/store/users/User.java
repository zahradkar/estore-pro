package eu.martin.store.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Setter
    private String name;

    @Setter
    private String email;

    @Setter
    private String password;

    @OneToMany(cascade = REMOVE)
    @JoinColumn(name = "user_id")
    private Set<Address> addresses = new HashSet<>();

    void addAddress(Address address) {
        addresses.add(address);
    }
}
