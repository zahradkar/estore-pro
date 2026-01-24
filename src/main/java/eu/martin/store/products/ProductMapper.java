package eu.martin.store.products;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
interface ProductMapper {
    Product toEntity(ProductRegisterDto dto);

    ProductResponse toResponse(Product product);

    @Mapping(source = "product.id", target = "id")
    @Mapping(source = "product.quantity", target = "newQuantity")
    @Mapping(source = "buyLog.buyPrice", target = "lastBuyPrice")
    @Mapping(source = "buyLog.supplier", target = "lastSupplier")
    ProductBuyResponse toProductBuyResponse(Product product, BuyLog buyLog);

    void update(ProductUpdateDto dto, @MappingTarget Product product);
}
