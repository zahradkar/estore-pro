package eu.martin.store.product;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
class ProductService {
    private ProductRepository repository;
    private ProductMapper mapper;

    ProductResponse registerNewProduct(ProductRequest dto) {
        // todo test
        var savedProduct = repository.save(mapper.toEntity(dto));
        return mapper.toResponse(savedProduct);
    }

    ProductResponse buyProduct(ProductRequest dto) {
        // todo
        var saved = repository.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }
}
