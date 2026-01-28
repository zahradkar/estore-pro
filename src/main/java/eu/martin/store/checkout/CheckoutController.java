package eu.martin.store.checkout;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    CheckoutResponse checkout(@RequestBody @Valid CheckoutRequest request) {
        return checkoutService.checkout(request);
    }
}
