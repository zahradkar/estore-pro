package eu.martin.store.cart;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("${app.cart-path}")
@RestController
class CartController {
    private final CartService service;

    @Value("${app.cart-path}")
    private String cartPath;

    @Operation(summary = "Creates a cart.")
    @PostMapping
    ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) { // test passed
        var cartResponse = service.createCart();
        var uri = uriBuilder.path(cartPath + "/{id}").buildAndExpand(cartResponse.id()).toUri();

        return ResponseEntity.created(uri).body(cartResponse);
    }

    @Operation(summary = "Returns given cartÏ.")
    @GetMapping("/{id}")
    ResponseEntity<CartDto> getCart(@PathVariable @Valid @NotNull UUID id) { // test passed
        return ResponseEntity.ok(service.getCart(id));
    }

    @Operation(summary = "Adds an item to given cart.")
    @PostMapping("/{cartId}/items")
    ResponseEntity<CartItemResponse> addToCart(@PathVariable UUID cartId, @RequestBody @Valid CartItemRequest dto) { // test passed
        return ResponseEntity.ok(service.addToCart(cartId, dto));
    }

    @Operation(summary = "Updates an item in given cart.")
    @PutMapping("/{cartId}/items/{productId}")
    CartItemResponse updateItem(@PathVariable UUID cartId, @PathVariable Integer productId, @RequestBody @Valid UpdateCartItemRequest request) {// test passed
        return service.updateItem(cartId, productId, request.quantity());
    }

    @Operation(summary = "Removes an item from given cart.")
    @DeleteMapping("/{cartId}/items/{productId}")
    ResponseEntity<Void> removeItem(@PathVariable UUID cartId, @PathVariable("productId") Integer productId) { // test passed
        service.removeItem(cartId, productId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Removes all items from given cart.")
    @DeleteMapping("/{cartId}/items")
    ResponseEntity<Void> clearCart(@PathVariable UUID cartId) { // test passed
        service.clearCart(cartId);

        return ResponseEntity.noContent().build();
    }
}
