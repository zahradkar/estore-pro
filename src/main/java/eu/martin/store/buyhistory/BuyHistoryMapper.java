package eu.martin.store.buyhistory;

import eu.martin.store.products.ProductBuyRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BuyHistoryMapper {
    BuyHistory toEntity(ProductBuyRequest dto);
}
