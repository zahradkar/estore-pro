package eu.martin.store.checkout;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    CheckoutResponse checkout(@RequestBody @Valid CheckoutRequest request) {
        return checkoutService.checkout(request);
    }

    @PostMapping("/webhook")
    public void handleWebhook(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }
}
