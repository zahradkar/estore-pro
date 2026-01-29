package eu.martin.store.products;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static eu.martin.store.products.MeasureUnit.UNIT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductMapperTest {
    private final ProductMapper mapper = new ProductMapperImpl();

    @Test
    void toResponse() {
        var id = 4;
        var name = "name";
        var description = "description";
        var sellPrice = BigDecimal.valueOf(35.2);
        int quantity = 54;
        var unit = UNIT;
        var product = new Product();
        ReflectionTestUtils.setField(product, "id", id);
        product.setDescription(description);
        product.setName(name);
        product.setMeasureUnit(unit);
        product.setQuantity(quantity);
        product.setSellPrice(sellPrice);

        var result = mapper.toResponse(product);

        assertEquals(id, result.id());
        assertEquals(name, result.name());
        assertEquals(description, result.description());
        assertEquals(sellPrice, result.sellPrice());
        assertEquals(quantity, result.quantity());
        assertEquals(unit, result.measureUnit());
    }
}