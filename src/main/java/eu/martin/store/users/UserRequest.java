package eu.martin.store.users;

record UserRequest(
        String name,
        String email,
        String password
) {
}
