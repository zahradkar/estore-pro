package eu.martin.store.checkout;


import eu.martin.store.orders.Order;

import java.util.Optional;

interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
