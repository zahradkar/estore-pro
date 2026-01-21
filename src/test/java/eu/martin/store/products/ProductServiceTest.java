package eu.martin.store.products;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static eu.martin.store.products.MeasureUnit.KILOGRAM;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = ProductService.class)
@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @Autowired
    private ProductService service;
    @MockitoBean
    private ProductRepository productRepository;
    @MockitoBean
    private ProductMapper productMapper;
    @MockitoBean
    private BuyHistoryRepository buyHistoryRepository;
    @MockitoBean
    private BuyHistoryMapper buyHistoryMapper;

    @Test
    void registerNewProduct() {
        // todo update
        var product = new Product();

        var result = productRepository.save(product);

        assertNotNull(result.getId());
    }

    @Test
    void buyProduct() {
        // todo
        // arrange
        var id = 1;
        var productWithId = getBasicProduct();
        ReflectionTestUtils.setField(productWithId, "id", id);
        when(productRepository.findById(id)).thenReturn(Optional.of(productWithId));
//        when(productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product id not found!"))).thenReturn(productWithId);

        when(productRepository.save(productWithId)).thenReturn(productWithId);

        when(buyHistoryMapper.toEntity(getBasicProductRequestDto())).thenReturn(getBasicBuyInfo());

        var savedBuyInfo = getBasicBuyInfo();
        ReflectionTestUtils.setField(savedBuyInfo, "id", 1L);
        when(buyHistoryRepository.save(getBasicBuyInfo())).thenReturn(savedBuyInfo);

        // act
        var result = service.buyProduct(id, getBasicProductRequestDto());

        // assert
        assertNotNull(result);
    }

    private Product getBasicProduct() {
        var product = new Product();
        product.setName("product name");
        product.setDescription("product's description");
        product.setSellPrice(BigDecimal.valueOf(35.23));
        product.setQuantity((short) 1);
        product.setMeasureUnit(KILOGRAM);
        return product;
    }

    private ProductBuyRequest getBasicProductRequestDto() {
        return new ProductBuyRequest((short) 3, BigDecimal.valueOf(43.2), "product's supplier");
    }

    private BuyHistory getBasicBuyInfo() {
        var buyInfo = new BuyHistory();
        buyInfo.setSupplier("product's supplier");
        buyInfo.setBuyPrice(BigDecimal.valueOf(593.2));
        return buyInfo;
    }
}