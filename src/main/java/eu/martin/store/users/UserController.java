package eu.martin.store.users;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("${app.user-path}")
@RestController
class UserController {
    private final UserService service;

    @Value("${app.user-path}")
    private String userPath;

    @Operation(summary = "Saves user (user registration).")
    @PostMapping
    ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest dto, UriComponentsBuilder uriBuilder) { // test passed
        var response = service.registerUser(dto);

        var uri = uriBuilder.path(userPath + "/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "Returns user by ID.")
    @GetMapping("/{id}")
    ResponseEntity<UserResponse> getUser(@PathVariable Integer id) { // test passed
        return ResponseEntity.ok(service.getUser(id));
    }

    @Operation(summary = "Returns all users. (only for admins)")
    @GetMapping
    ResponseEntity<Iterable<UserResponse>> getAllUsers(@RequestParam(required = false, defaultValue = "", name = "sort") String sortBy) { // test passed
        return ResponseEntity.ok(service.getAllUsers(sortBy));
    }

    @Operation(summary = "Updates user by ID.")
    @PutMapping("/{id}")
    ResponseEntity<UserResponse> updateUser(@PathVariable Integer id, @RequestBody UserRequest dto) { // test passed
        return ResponseEntity.ok(service.updateUser(id, dto));
    }

    @Operation(summary = "Deletes user by ID.")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Integer id) { // test passed
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Changes user password.")
    @PostMapping("/{id}/change-password")
    void changePassword(@PathVariable Integer id, @Valid @RequestBody ChangePasswordRequest dto) { // test passed
        service.changePassword(id, dto);
    }

    @Operation(summary = "Updates user profile.")
    @PutMapping("/{id}/profile")
    ResponseEntity<ProfileResponse> updateProfile(@PathVariable("id") Long userId, @RequestBody ProfileRequest dto) { // test passed
        // profile is created at user registration
        return ResponseEntity.ok(service.updateProfile(userId, dto));
    }

    @Operation(summary = "Returns user profile by ID.")
    @GetMapping("/{id}/profile")
    ResponseEntity<ProfileResponse> getProfile(@PathVariable("id") Long userId) { // test passed
        return ResponseEntity.ok(service.getProfile(userId));
    }

    /**
     * Verifies user's e-mail address using JWT<br>
     * flow:<br>
     * 1. User registers himself and validation e-mail is sent to his e-address.<br>
     * 2. When the user confirms (verifies) the e-address, his/her account is enabled.
     */

    @Operation(summary = "Verifies user's e-mail.")
    @GetMapping("/verify/{token}")
    ResponseEntity<String> verifyUserEmail(@PathVariable String token) {
        String location = service.verifyEmail(token) ? "/email-verification-success.html" : "/email-verification-fail.html";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", location);

        // Note the use of HttpStatus.FOUND for redirect
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @Operation(summary = "Sends new verification token when user asks for it. (available only if the previous one is expired)")
    @GetMapping("/resend-token/{email}")
    ResponseEntity<String> resendVerificationToken(@PathVariable String email) {
        return ResponseEntity.ok(service.resendVerificationToken(email));
    }
}
