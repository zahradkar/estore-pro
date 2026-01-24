package eu.martin.store.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("${app.admin-path}")
@RestController
record AdminController() {
    @GetMapping
    String test() { // test passed
        return "Admin authorization works correctly!";
    }
}
