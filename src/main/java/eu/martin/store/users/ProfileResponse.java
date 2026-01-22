package eu.martin.store.users;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Profile}
 */
record ProfileResponse(
        String bio,
        String phoneNumber,
        LocalDate dateOfBirth,
        Integer loyaltyPoints,
        UserResponse user // id stored in UserResponse
) implements Serializable {
}