package eu.martin.store.checkout;

import eu.martin.store.auth.AuthService;
import eu.martin.store.cart.CartRepository;
import eu.martin.store.cart.CartService;
import eu.martin.store.orders.Order;
import eu.martin.store.orders.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static eu.martin.store.common.Utils.CART_IS_EMPTY;
import static eu.martin.store.common.Utils.CART_NOT_FOUND;

@AllArgsConstructor
@Service
class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;

    @Transactional
    CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.getCartWithItems(request.cartId()).orElseThrow(() -> new CartException(CART_NOT_FOUND));

        if (cart.isEmpty())
            throw new CartException(CART_IS_EMPTY);

        var order = Order.fromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);

        try {
            var session = paymentGateway.createCheckoutSession(order);

            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(), session.checkoutUrl());
        } catch (PaymentException exception) {
            orderRepository.delete(order);
            throw exception;
        }
    }

    void handleWebhookEvent(WebhookRequest request) {
        paymentGateway
                .parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
                    var order = orderRepository.findById(paymentResult.orderId()).orElseThrow();
                    order.setStatus(paymentResult.paymentStatus());
                    orderRepository.save(order);
                });
    }
}
