package eu.martin.store.buyhistory;

import eu.martin.store.product.ProductBuyRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BuyInfoMapper {
    BuyInfo toEntity(ProductBuyRequest dto);
}
