package eu.martin.store.orders;

import eu.martin.store.auth.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

import static eu.martin.store.common.Utils.ORDER_NOT_FOUND;

@AllArgsConstructor
@Service
class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    List<OrderDto> getAllOrders() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.getAllOrdersByCustomer(user);
        return orderMapper.toDtos(orders);
    }

    OrderDto getOrder(long orderId) {
        var order = orderRepository.getOrderWithItems(orderId).orElseThrow(() -> new EntityNotFoundException(ORDER_NOT_FOUND));

        var user = authService.getCurrentUser();
        if (!order.isPlacedBy(user))
            throw new AccessDeniedException("You don't have access to this order!");

        return orderMapper.toDto(order);
    }
}
