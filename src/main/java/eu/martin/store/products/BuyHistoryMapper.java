package eu.martin.store.products;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface BuyHistoryMapper {
    BuyHistory toEntity(ProductBuyRequest dto);
}
