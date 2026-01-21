package eu.martin.store.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
record CartController(CartService service) {

    @PostMapping
    ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) { // test passed
        var cartResponse = service.createCart();
        var uri = uriBuilder.path("/api/carts/{id}").buildAndExpand(cartResponse.id()).toUri();

        return ResponseEntity.created(uri).body(cartResponse);
    }

    @GetMapping("/{id}")
    ResponseEntity<CartDto> getCart(@PathVariable @Valid @NotNull UUID id) { // test passed
        return ResponseEntity.ok(service.getCart(id));
    }

    @PostMapping("/{cartId}/items")
    ResponseEntity<CartItemResponse> addToCart(@PathVariable("cartId") UUID cartId, @RequestBody @Valid CartItemRequest dto) { // test passed
        return ResponseEntity.ok(service.addToCart(cartId, dto));
    }

    @PutMapping("/{cartId}/items/{productId}")
    CartItemResponse updateItem( // test passed
            @PathVariable("cartId") UUID cartId,
            @PathVariable @Valid @NotNull @Min(1) Integer productId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        return service.updateItem(cartId, productId, request.quantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    ResponseEntity<Void> removeItem(@PathVariable("cartId") UUID cartId, @PathVariable("productId") @Valid @NotNull @Min(1) Integer productId) { // test passed
        service.removeItem(cartId, productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    ResponseEntity<Void> clearCart(@PathVariable UUID cartId) { // test passed
        service.clearCart(cartId);

        return ResponseEntity.noContent().build();
    }
}
