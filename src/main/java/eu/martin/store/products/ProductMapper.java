package eu.martin.store.products;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
interface ProductMapper {
    Product toEntity(ProductRegisterDto dto);

    ProductResponse toResponse(Product product);

    void update(ProductUpdateDto dto, @MappingTarget Product product);
}
