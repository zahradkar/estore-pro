package eu.martin.store.product;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
interface ProductMapper {
    Product toEntity(ProductRegisterDto dto);

    ProductResponse toResponse(Product product);

    void update(ProductUpdateDto dto, @MappingTarget Product product);
}
