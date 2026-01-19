package eu.martin.store.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductServiceTest {

    @Autowired
    private ProductRepository repository;
    @Mock
    private ProductMapperImpl mapper;

    @Test
    void registerNewProduct() {
        var product = new Product();

        var result = repository.save(product);

        assertNotNull(result.getId());
    }

    @Test
    void buyProduct() {
    }
}