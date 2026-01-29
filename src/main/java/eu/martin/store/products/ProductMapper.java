package eu.martin.store.products;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(componentModel = "spring")
interface ProductMapper {

    // handles requests
    @Mapping(expression = "java(toAttributes(dto.attributeDtos()))", target = "attributes")
    Product toProduct(ProductController.ProductWithAttribsDto dto);

    Attribute toAttribute(ProductController.AttributeDto dto);

    Set<Attribute> toAttributes(Set<ProductController.AttributeDto> dtos);
    // ^ handles requests ^

    // handles responses
    @Mapping(expression = "java(toAttributeDtos(product.getAttributes()))", target = "attributeDtos")
    ProductController.ProductWithAttribsDto toProductWithAttributesDto(Product product);

    ProductController.AttributeDto toAttributeDto(Attribute attribute);

    Set<ProductController.AttributeDto> toAttributeDtos(Set<Attribute> attributes);
    // ^ handles responses ^

    @Mapping(source = "product.id", target = "id")
    @Mapping(source = "product.quantity", target = "newQuantity")
    @Mapping(source = "buyLog.buyPrice", target = "lastBuyPrice")
    @Mapping(source = "buyLog.supplier", target = "lastSupplier")
    ProductBuyResponse toProductBuyResponse(Product product, BuyLog buyLog);

    @Mapping(expression = "java(toAttributes(dto.attributeDtos()))", target = "attributes")
    void update(ProductController.ProductWithAttribsDto dto, @MappingTarget Product product);
}
