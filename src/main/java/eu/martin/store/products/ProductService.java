package eu.martin.store.products;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    BuyLogResponse getProductBuyLogs(Integer productId) {
        if (!productRepository.existsById(productId))
            throw new EntityNotFoundException(PRODUCT_NOT_FOUND);

        List<BuyLog> productBuyLogs = buyLogRepository.getProductBuyLogs(productId);

        return buyLogMapper.toResponse(productBuyLogs);
    }

    List<ProductController.ProductSpecsResponse> getSortedProductsBySpecification(short pageNumber, short pageSize, String name, BigDecimal minPrice, BigDecimal maxPrice, String attributeName, String productProperty, boolean descending) {
        var specification = new ArrayList<Specification<Product>>(4);
        Sort sort;

        if (name != null)
            specification.add(ProductSpecifications.hasName(name));
        if (minPrice != null)
            specification.add(ProductSpecifications.hasPriceGreaterThanOrEqualTo(minPrice));
        if (maxPrice != null)
            specification.add(ProductSpecifications.hasPriceLessThanOrEqualTo(maxPrice));
        if (attributeName != null)
            specification.add(ProductSpecifications.hasAttribute(attributeName));

        if (productProperty == null)
            sort = Sort.unsorted();
        else if (descending)
            sort = Sort.by(productProperty).descending();
        else
            sort = Sort.by(productProperty);

        var sortedProductsBySpecification = productRepository.findAll(Specification.allOf(specification), PageRequest.of(pageNumber, pageSize, sort)).getContent();

        return productMapper.toSpecsResponse(sortedProductsBySpecification);
    }
}
