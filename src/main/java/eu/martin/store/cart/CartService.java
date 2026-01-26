package eu.martin.store.cart;

import eu.martin.store.products.Product;
import eu.martin.store.products.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static eu.martin.store.common.Utils.CART_NOT_FOUND;
import static eu.martin.store.common.Utils.PRODUCT_NOT_FOUND;

@AllArgsConstructor
@Service
class CartService {
    private final CartRepository cartRepository;
    private final CartMapper mapper;
    private final ProductRepository productRepository;

    CartDto createCart() {
        var cart = cartRepository.save(new Cart());

        return mapper.toResponse(cart);
    }

    CartDto getCart(UUID id) {
        var cart = getCartById(id);
        return mapper.toResponse(cart);
    }

    private Cart getCartById(UUID id) {
        return cartRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(CART_NOT_FOUND));
    }

    CartItemResponse addToCart(UUID cartId, CartItemRequest dto) {
        var cart = getCartWithItems(cartId);
        var product = getProduct(dto.productId());

        var cartItem = cart.addItem(product, dto.quantity());

        cartRepository.save(cart);

        return mapper.toItemDto(cartItem);
    }

    private Cart getCartWithItems(UUID cartId) {
        return cartRepository.getCartWithItems(cartId).orElseThrow(() -> new EntityNotFoundException(CART_NOT_FOUND));
    }

    private Product getProduct(int productId) {
        return productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));
    }

    CartItemResponse updateItem(UUID cartId, int productId, short quantity) {
        var cart = getCartWithItems(cartId);
        var cartItem = cart.getItem(productId);
        if (cartItem == null)
            throw new ItemNotFoundException();

        var product = getProduct(productId);
        if (quantity > product.getQuantity())
            throw new QuantityExceedException();

        cartItem.setQuantity((int) quantity);
        cartRepository.save(cart);

        return mapper.toItemDto(cartItem);
    }

    void removeItem(UUID cartId, int productId) {
        var cart = getCartWithItems(cartId);

        cart.removeItem(productId);

        cartRepository.save(cart);
    }

    void clearCart(UUID cartId) {
        var cart = getCartWithItems(cartId);

        cart.clear();

        cartRepository.save(cart);
    }
}
