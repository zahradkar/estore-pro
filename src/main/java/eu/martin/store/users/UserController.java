package eu.martin.store.users;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/api/users")
record UserController(UserService service) {

    @PostMapping
    ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest dto, UriComponentsBuilder uriBuilder) {
        // todo test
        var response = service.registerUser(dto);
        var uri = uriBuilder.path("/api/users/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> getUserById(@PathVariable @Valid @NotNull @Min(1) Integer id) {
        // todo test
        return ResponseEntity.ok(service.getUserById(id));
    }

    @GetMapping
    ResponseEntity<Iterable<UserResponse>> getAllUsers(@RequestParam(required = false, defaultValue = "", name = "sort") String sortBy) {
        return ResponseEntity.ok(service.getAllUsers(sortBy));
    }

    @PutMapping("/{id}")
    ResponseEntity<UserResponse> updateUser(@PathVariable @Valid @NotNull @Min(1) Integer id, @RequestBody UserRequest dto) {
        return ResponseEntity.ok(service.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable @Valid @NotNull @Min(1) Integer id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
