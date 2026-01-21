package eu.martin.store.cart;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
interface CartMapper {
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toResponse(Cart cart); // test passed

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemResponse toItemDto(CartItem cartItem); // test passed
}
