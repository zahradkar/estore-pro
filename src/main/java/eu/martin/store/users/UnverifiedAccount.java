package eu.martin.store.users;

import java.time.LocalDateTime;

/**
 * Projection for {@link User}
 */
interface UnverifiedAccount {
	String getEmail();

	LocalDateTime getRegisteredAt();
}