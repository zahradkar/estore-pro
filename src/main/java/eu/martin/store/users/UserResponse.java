package eu.martin.store.users;

record UserResponse(
        long id,
        String name,
        String email,
        String password
) {
}
