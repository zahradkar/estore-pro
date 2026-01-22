package eu.martin.store.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Setter
    private String bio;

    @Setter
    private String phoneNumber;

    @Setter
    private LocalDate dateOfBirth;

    @Setter
    private Integer loyaltyPoints;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    @MapsId
    private User user;
}
