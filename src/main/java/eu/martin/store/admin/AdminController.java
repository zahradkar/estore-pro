package eu.martin.store.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/admin")
@RestController
record AdminController() {
    @GetMapping("/test")
    String sayHello() {
        return "result of test admin authorization";
    }
}
