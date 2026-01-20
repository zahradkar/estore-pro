package eu.martin.store.product;

import eu.martin.store.buyinfo.BuyInfoMapper;
import eu.martin.store.buyinfo.BuyInfoRepository;
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
    private BuyInfoRepository buyInfoRepository;
    private ProductMapper productMapper;
    private BuyInfoMapper buyInfoMapper;

    ProductResponse registerNewProduct(ProductRegisterDto dto) {
        var savedProduct = productRepository.save(productMapper.toEntity(dto));
        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    ProductBuyResponse buyProduct(Integer id, ProductBuyRequest dto) {
        var product = getProduct(id);
        product.increaseQuantity(dto.quantity());

        var buyInfo = buyInfoMapper.toEntity(dto);
        buyInfo.setProduct(product);
        buyInfo = buyInfoRepository.save(buyInfo);

        return new ProductBuyResponse(product.getId(), product.getName(), product.getQuantity(), buyInfo.getBuyPrice(), buyInfo.getSupplier());
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
