package eu.martin.store.users;

public record UserResponse(
        long id,
        String name,
        String email
) {
}
