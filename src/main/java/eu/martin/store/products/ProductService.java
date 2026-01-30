package eu.martin.store.products;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static eu.martin.store.common.Utils.PRODUCT_NOT_FOUND;

@Slf4j
@AllArgsConstructor
@Service
class ProductService {
    private ProductRepository productRepository;
    private BuyLogRepository buyLogRepository;
    private ProductMapper productMapper;
    private BuyLogMapper buyLogMapper;

    @Transactional
    ProductController.ProductWithAttribsDto createProductWithAttributes(ProductController.ProductWithAttribsDto dto) {
        var product = productMapper.toProduct(dto);

        var createdProduct = productRepository.save(product);

        return productMapper.toProductWithAttributesDto(createdProduct);
    }

    @Transactional
    ProductController.ProductBuyResponse buyProduct(Integer id, ProductController.ProductBuyRequest dto) {
        var product = getProduct(id);
        product.increaseQuantity(dto.quantity());

        var buyLog = buyLogMapper.toBuyLog(dto, product);
        buyLog = buyLogRepository.save(buyLog);

        return productMapper.toProductBuyResponse(product, buyLog);
    }

    ProductController.ProductWithAttribsDto getProductById(Integer id) {
        var product = getProduct(id);
        return productMapper.toProductWithAttributesDto(product);
    }

    private Product getProduct(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));
    }

    ProductController.ProductWithAttribsDto update(ProductController.ProductWithAttribsDto dto) {
        if (dto.id() == null)
            throw new IllegalArgumentException("On update product, ID must be provided!");
        var product = getProduct(dto.id());
        productMapper.update(dto, product);

        var updatedProduct = productRepository.save(product);

        return productMapper.toProductWithAttributesDto(updatedProduct);
    }

    Page<Product> getProtuctsPage(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }

    BuyLogResponse getProductBuyLogs(Integer productId) {
        if (!productRepository.existsById(productId))
            throw new EntityNotFoundException(PRODUCT_NOT_FOUND);

        List<BuyLog> productBuyLogs = buyLogRepository.getProductBuyLogs(productId);

        return buyLogMapper.toResponse(productBuyLogs);
    }
}
