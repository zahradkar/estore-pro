package eu.martin.store.products;

import eu.martin.store.buyhistory.BuyHistoryMapper;
import eu.martin.store.buyhistory.BuyHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
class ProductService {
    private ProductRepository productRepository;
    private BuyHistoryRepository buyHistoryRepository;
    private ProductMapper productMapper;
    private BuyHistoryMapper buyHistoryMapper;

    ProductResponse registerNewProduct(ProductRegisterDto dto) {
        var savedProduct = productRepository.save(productMapper.toEntity(dto));
        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    ProductBuyResponse buyProduct(Integer id, ProductBuyRequest dto) {
        var product = getProduct(id);
        product.increaseQuantity(dto.quantity());

        var buyHistory = buyHistoryMapper.toEntity(dto);
        buyHistory.setProduct(product);
        buyHistory = buyHistoryRepository.save(buyHistory);

        return new ProductBuyResponse(product.getId(), product.getName(), product.getQuantity(), buyHistory.getBuyPrice(), buyHistory.getSupplier());
    }

    ProductResponse getProductById(Integer id) {
        var product = getProduct(id);
        return productMapper.toResponse(product);
    }

    private Product getProduct(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product id not found!"));
    }

    ProductResponse update(Integer id, ProductUpdateDto dto) {
        var product = getProduct(id);
        productMapper.update(dto, product);
        return productMapper.toResponse(product);
    }

    Page<Product> getProtuctsPage(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }
}
