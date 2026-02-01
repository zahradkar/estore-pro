package eu.martin.store.products;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static eu.martin.store.common.Utils.CATEGORY_NOT_FOUND;
import static eu.martin.store.common.Utils.PRODUCT_NOT_FOUND;

@Slf4j
@AllArgsConstructor
@Service
class ProductService {
    private final CategoryRepository categoryRepository;
    private final JsonMapper jsonMapper;
    private ProductRepository productRepository;
    private BuyLogRepository buyLogRepository;
    private ProductMapper productMapper;
    private BuyLogMapper buyLogMapper;

    @Transactional
    ProductController.ProductWithAttribsDto createProductWithAttributes(ProductController.ProductWithAttribsDto dto) {
        var product = productMapper.toProduct(dto);
        product.setCategories(getCategories(dto.categorySummaries()));

        var createdProduct = productRepository.save(product);

        return productMapper.toProductWithAttributesDto(createdProduct);
    }

    private Set<Category> getCategories(Set<ProductController.ProductWithAttribsDto.CategorySummary> categorySummaries) {
        return new HashSet<>(categoryRepository.findAllById(
                categorySummaries.stream()
                        .map(ProductController.ProductWithAttribsDto.CategorySummary::id)
                        .toList()
        ));
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

    ProductController.ProductWithAttribsDto updateProduct(ProductController.ProductWithAttribsDto dto) {
        if (dto.id() == null)
            throw new IllegalArgumentException("On update product, ID must be provided!");

        var product = getProduct(dto.id());

        productMapper.update(dto, product);
        product.setCategories(getCategories(dto.categorySummaries()));

        var updatedProduct = productRepository.save(product);

        return productMapper.toProductWithAttributesDto(updatedProduct);
    }

    BuyLogResponse getProductBuyLogs(Integer productId) {
        if (!productRepository.existsById(productId))
            throw new EntityNotFoundException(PRODUCT_NOT_FOUND);

        List<BuyLog> productBuyLogs = buyLogRepository.getProductBuyLogs(productId);

        return buyLogMapper.toResponse(productBuyLogs);
    }

    List<ProductController.ProductSpecsResponse> getSortedProductsBySpecification(short pageNumber, short pageSize, String name, BigDecimal minPrice, BigDecimal maxPrice, String attributeName, String categoryName, String productProperty, boolean descending) {
        var specification = new ArrayList<Specification<Product>>(5);
        Sort sort;

        if (name != null)
            specification.add(ProductSpecifications.hasName(name));
        if (minPrice != null)
            specification.add(ProductSpecifications.hasPriceGreaterThanOrEqualTo(minPrice));
        if (maxPrice != null)
            specification.add(ProductSpecifications.hasPriceLessThanOrEqualTo(maxPrice));
        if (attributeName != null)
            specification.add(ProductSpecifications.hasAttribute(attributeName));
        if (categoryName != null)
            specification.add(ProductSpecifications.hasCategory(categoryName));

        if (productProperty == null)
            sort = Sort.unsorted();
        else if (descending)
            sort = Sort.by(productProperty).descending();
        else
            sort = Sort.by(productProperty);

        var sortedProductsBySpecification = productRepository.findAll(Specification.allOf(specification), PageRequest.of(pageNumber, pageSize, sort)).getContent();

        return productMapper.toSpecsResponse(sortedProductsBySpecification);
    }

    ProductController.CategoryDto createCategory(ProductController.CategoryDto dto) {
        var category = productMapper.toCategory(dto);

        var savedCategory = categoryRepository.save(category);

        return productMapper.toCategoryDto(savedCategory);
    }

    ProductController.CategoryDto getCategory(short id) {
        return productMapper.toCategoryDto(categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND)));
    }

    ProductController.CategoryDto updateCategory(ProductController.CategoryDto dto) {
        if (dto.id() == null || dto.id() < 1 || dto.id() > 1000)
            throw new IllegalArgumentException("On category update, ID must be between 1 and 1000 including!");

        var category = categoryRepository.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND));

        productMapper.update(dto, category);

        return productMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    void importCategories(MultipartFile file) {
        try {
            var categoryTree = jsonMapper.readValue(
                    file.getInputStream(),
                    new TypeReference<List<ProductController.CategoryImportDto>>() {
                    }
            );

            processCategories(categoryTree);
        } catch (IOException e) {
            throw new InvalidFormatException("Failed to parse category file: " + e.getMessage());
        }
    }

    // due to optimization of database calls, used Breadth-First Traversal (Level by Level)
    private void processCategories(List<ProductController.CategoryImportDto> rootDtos) {
        if (rootDtos == null || rootDtos.isEmpty())
            return;

        var categoryCache = categoryRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Category::getName, Function.identity()));

        var currentLevel = new ArrayList<ProcessingNode>();

        for (var dto : rootDtos)
            currentLevel.add(new ProcessingNode(dto, null));

        while (!currentLevel.isEmpty()) {
            var batchToSave = new ArrayList<Category>();
            var nextLevel = new ArrayList<ProcessingNode>();

            for (var node : currentLevel) {
                var dto = node.dto;

                var category = categoryCache.getOrDefault(dto.name(), Category.of(dto.name()));

                category.setDescription(dto.description());
                category.setParent(node.parent);

                batchToSave.add(category);
            }

            var savedMap = categoryRepository.saveAll(batchToSave)
                    .stream()
                    .collect(Collectors.toMap(Category::getName, Function.identity()));

            categoryCache.putAll(savedMap);

            for (var node : currentLevel) {
                var parentEntity = savedMap.get(node.dto.name());

                if (node.dto.children() != null)
                    for (var childDto : node.dto.children())
                        nextLevel.add(new ProcessingNode(childDto, parentEntity));
            }

            currentLevel = nextLevel;
        }
    }

    private record ProcessingNode(
            ProductController.CategoryImportDto dto,
            Category parent
    ) {
    }
}
