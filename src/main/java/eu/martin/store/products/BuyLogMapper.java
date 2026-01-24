package eu.martin.store.products;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
interface BuyLogMapper {
    BuyLog toEntity(ProductBuyRequest dto);

    default BuyLogResponse toResponse(List<BuyLog> buyLogs) {
        if (buyLogs == null || buyLogs.isEmpty())
            return null;

        Product product = buyLogs.getFirst().getProduct();

        List<BuyLogResponse.BuyLog> logs = toInnerLogs(buyLogs);

        return new BuyLogResponse(product, logs);
    }

    List<BuyLogResponse.BuyLog> toInnerLogs(List<BuyLog> buyLogs);

    BuyLogResponse.BuyLog toInnerLog(BuyLog buyLog);
}
