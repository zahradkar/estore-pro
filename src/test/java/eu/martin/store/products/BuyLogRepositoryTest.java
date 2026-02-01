package eu.martin.store.products;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BuyLogRepositoryTest {
    @Autowired
    private BuyLogRepository logRepo;

    @Autowired
    private ProductRepository productRepo;

    @Test
    void getProductBuyLogs() {
        var product = getBasicProduct();
        var savedProduct = productRepo.save(product);

        var buyLog1 = new BuyLog();
        buyLog1.setSupplier("supp 1");
        buyLog1.setBuyPrice(BigDecimal.valueOf(83.22));
        buyLog1.setProduct(savedProduct);

        var buyLog2 = new BuyLog();
        buyLog2.setSupplier("supp 2");
        buyLog2.setBuyPrice(BigDecimal.valueOf(84.30));
        buyLog2.setProduct(savedProduct);

        var buyLog3 = new BuyLog();
        buyLog3.setSupplier("supp 3");
        buyLog3.setBuyPrice(BigDecimal.valueOf(62.84));
        buyLog3.setProduct(savedProduct);

        logRepo.saveAll(List.of(buyLog1, buyLog2, buyLog3));


        var result = logRepo.getProductBuyLogs(savedProduct.getId());

        assertEquals(3, result.size());
        assertEquals(BigDecimal.valueOf(83.22), result.getFirst().getBuyPrice());
        assertEquals(savedProduct, result.getFirst().getProduct());
        assertEquals("supp 1", result.getFirst().getSupplier());
    }

    private Product getBasicProduct() {
        var product = new Product();
        product.setName("product 1");
        product.setSellPrice(BigDecimal.valueOf(7.92));
        product.setQuantity(32);
        product.setMeasureUnit(MeasureUnit.UNIT);
        return product;
    }
}