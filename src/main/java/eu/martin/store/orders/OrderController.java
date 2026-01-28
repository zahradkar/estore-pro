package eu.martin.store.orders;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/orders")
@RequiredArgsConstructor
@RestController
class OrderController {
    private final OrderService service;

    @Operation(summary = "Returns all orders.") // todo check if auth required
    @GetMapping
    List<OrderDto> getAllOrders() {
        return service.getAllOrders();
    }

    @Operation(summary = "Returns given order (of currently logged in user).")
    @GetMapping("/{orderId}")
    OrderDto getOrder(@PathVariable Long orderId) {
        return service.getOrder(orderId);
    }
}
