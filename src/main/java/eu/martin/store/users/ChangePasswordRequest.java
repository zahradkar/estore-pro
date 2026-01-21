package eu.martin.store.users;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword
) {
}
