package eu.martin.store.buyhistory;

import eu.martin.store.product.ProductBuyRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BuyInfoMapperTest {

    private final BuyInfoMapper mapper = new BuyInfoMapperImpl();

    @Test
    void toEntity() {
        short quantity = 843;
        var buyPrice = 593.23f;
        var supplier = "a supplier";
        var dto = new ProductBuyRequest(quantity, buyPrice, supplier);

        var result = mapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(buyPrice, result.getBuyPrice());
        assertEquals(supplier, result.getSupplier());
    }
}