package eu.martin.store.users;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    @OneToMany(cascade = REMOVE)
    private Set<Address> addresses = new HashSet<>();
//    private String role;
}
