package eu.martin.store.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Setter
    private String street;

    @Setter
    private String city;

    @Setter
    private String zip;

    @Setter
    private String country;

    static Address of(String street, String city, String zip, String country) {
        var address = new Address();
        address.street = street;
        address.city = city;
        address.zip = zip;
        address.country = country;
        return address;
    }
}
