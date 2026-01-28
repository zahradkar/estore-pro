package eu.martin.store.admin;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("${app.admin-path}")
@RestController
record AdminController() {

    @Operation(summary = "Returns simple sentence (only for testing admin privileges).")
    @GetMapping
    String test() { // test passed
        return "Admin authorization works correctly!";
    }
}
