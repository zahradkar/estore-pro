package eu.martin.store.products;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static eu.martin.store.common.Utils.PRODUCT_NOT_FOUND;

@AllArgsConstructor
@Service
class ProductService {
    private ProductRepository productRepository;
    private BuyLogRepository buyLogRepository;
    private ProductMapper productMapper;
    private BuyLogMapper buyLogMapper;

    ProductResponse registerNewProduct(ProductRegisterDto dto) {
        var savedProduct = productRepository.save(productMapper.toEntity(dto));
        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    ProductBuyResponse buyProduct(Integer id, ProductBuyRequest dto) {
        var product = getProduct(id);
        product.increaseQuantity(dto.quantity());

        var buyLog = buyLogMapper.toEntity(dto);
        buyLog.setProduct(product);
        buyLog = buyLogRepository.save(buyLog);

        return productMapper.toProductBuyResponse(product, buyLog);
    }

    ProductResponse getProductById(Integer id) {
        var product = getProduct(id);
        return productMapper.toResponse(product);
    }

    private Product getProduct(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));
    }

    ProductResponse update(Integer id, ProductUpdateDto dto) {
        var product = getProduct(id);
        productMapper.update(dto, product);

        var updatedProduct = productRepository.save(product);

        return productMapper.toResponse(updatedProduct);
    }

    Page<Product> getProtuctsPage(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }

    BuyLogResponse getProductBuyLogs(Integer productId) {
        List<BuyLog> productBuyLogs = buyLogRepository.getProductBuyLogs(productId);
        return buyLogMapper.toResponse(productBuyLogs);
    }
}
