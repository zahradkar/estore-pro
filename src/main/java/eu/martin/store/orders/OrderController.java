package eu.martin.store.orders;

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

    @GetMapping
    List<OrderDto> getAllOrders() {
        return service.getAllOrders();
    }

    @GetMapping("/{orderId}")
    OrderDto getOrder(@PathVariable Long orderId) {
        return service.getOrder(orderId);
    }
}
