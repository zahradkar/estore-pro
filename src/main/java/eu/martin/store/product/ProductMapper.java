package eu.martin.store.product;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface ProductMapper {
    Product toEntity(ProductRequest dto);

    ProductResponse toResponse(Product product);
}
