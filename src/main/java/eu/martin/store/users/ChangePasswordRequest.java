package eu.martin.store.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record ChangePasswordRequest(
        @NotBlank String oldPassword,
        @NotBlank @Size(min = 5, max = 30) String newPassword // same as used in UserRequest
) {
}
