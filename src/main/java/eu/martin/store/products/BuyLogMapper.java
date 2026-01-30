package eu.martin.store.products;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
interface BuyLogMapper {
    @Mapping(ignore = true, target = "timestamp")
    BuyLog toBuyLog(ProductController.ProductBuyRequest dto, Product product);

    default BuyLogResponse toResponse(List<BuyLog> buyLogs) {
        if (buyLogs == null || buyLogs.isEmpty())
            return null;

        var summary = toProductSummary(buyLogs.getFirst().getProduct());
        var logs = toInnerLogs(buyLogs);

        return new BuyLogResponse(summary, logs);
    }

    BuyLogResponse.ProductSummary toProductSummary(Product product);

    List<BuyLogResponse.BuyLogDto> toInnerLogs(List<BuyLog> buyLogs);

    BuyLogResponse.BuyLogDto toInnerLog(BuyLog buyLog);
}
