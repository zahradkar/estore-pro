package eu.martin.store.users;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Profile}
 */
record ProfileRequest(
        String bio,
        String phoneNumber,
        LocalDate dateOfBirth,
        Integer loyaltyPoints
) implements Serializable {
}