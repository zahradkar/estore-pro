package eu.martin.store.buyhistory;

import eu.martin.store.products.ProductBuyRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BuyHistoryMapperTest {

    private final BuyHistoryMapper mapper = new BuyHistoryMapperImpl();

    @Test
    void toEntity() {
        short quantity = 843;
        var buyPrice = BigDecimal.valueOf(593.23);
        var supplier = "a supplier";
        var dto = new ProductBuyRequest(quantity, buyPrice, supplier);

        var result = mapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(buyPrice, result.getBuyPrice());
        assertEquals(supplier, result.getSupplier());
    }
}