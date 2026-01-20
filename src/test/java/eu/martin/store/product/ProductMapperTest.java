package eu.martin.store.product;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static eu.martin.store.product.MeasureUnit.UNIT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductMapperTest {
    private final ProductMapper mapper = new ProductMapperImpl();

    @Test
    void toEntity() {
        var name = "name";
        var description = "description";
        var sellPrice = 35.2f;
        var quantity = 54.0f;
        var unit = UNIT;
        var request = new ProductRegisterDto(name, description, sellPrice, quantity, unit);

        var result = mapper.toEntity(request);

        assertEquals(name, result.getName());
        assertEquals(description, result.getDescription());
        assertEquals(sellPrice, result.getSellPrice());
        assertEquals(quantity, result.getQuantity());
        assertEquals(unit, result.getMeasureUnit());
    }

    @Test
    void toResponse() {
        var id = 4;
        var name = "name";
        var description = "description";
        var sellPrice = 35.2f;
        short quantity = 54;
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