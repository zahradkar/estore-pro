package eu.martin.store.users;

record ChangePasswordRequest(
        String oldPassword,
        String newPassword
) {
}
