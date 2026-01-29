package eu.martin.store.products;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Set;

@RequiredArgsConstructor
@RequestMapping("${app.product-path}")
@RestController
class ProductController {
    private final ProductService service;

    @Value("${app.product-path}")
    private String productPath;

    @Operation(summary = "Registers new product (Creates product with attributes and saves it).")
    @PostMapping
    ResponseEntity<ProductWithAttribsDto> registerProductWithAttributes(@RequestBody @Valid ProductController.ProductWithAttribsDto dto, UriComponentsBuilder uriBuilder) {
        var result = service.createProductWithAttributes(dto);

        var uri = uriBuilder.path(productPath + "/{id}").buildAndExpand(result.id).toUri();

        return ResponseEntity.created(uri).body(result);
    }

    @Operation(summary = "Saves buy logs (Increases quantity of product, adds buy price and supplier and saves).")
    @PostMapping("/{id}")
    ResponseEntity<ProductBuyResponse> buyProduct(@PathVariable Integer id, @RequestBody @Valid ProductBuyRequest dto) { // test passed
        return ResponseEntity.ok(service.buyProduct(id, dto));
    }

    @Operation(summary = "Returns product by ID.")
    @GetMapping("/{id}")
    ResponseEntity<ProductWithAttribsDto> getProduct(@PathVariable Integer id) { // test passed
        return ResponseEntity.ok(service.getProductById(id));
    }

    @Operation(summary = "Updates product by ID.")
    @PutMapping
    ResponseEntity<ProductWithAttribsDto> updateProduct(@RequestBody @Valid ProductController.ProductWithAttribsDto dto) { // test passed
        // used also for adding/removing attributes to/from Product
        return ResponseEntity.ok(service.update(dto));
    }

    @Operation(summary = "Returns all products.")
    @GetMapping
    ResponseEntity<Page<Product>> getProducts(@RequestParam int page, @RequestParam int size) { // test passed
        return ResponseEntity.ok(service.getProtuctsPage(page, size));
    }

    @Operation(summary = "Returns buy logs/history of given product.")
    @GetMapping("/{id}/logs")
    ResponseEntity<BuyLogResponse> getProductBuyLogs(@PathVariable Integer id) { // test passed
        return ResponseEntity.ok(service.getProductBuyLogs(id));
    }


    record ProductWithAttribsDto(
            Integer id,
            @NotBlank String name,
            String description,
            @NotNull BigDecimal sellPrice,
            @NotNull Integer quantity,
            @NotNull MeasureUnit measureUnit,
            Set<@Valid AttributeDto> attributeDtos
    ) {
    }

    record AttributeDto(
            @NotBlank String name,
            String value,
            @NotNull Attribute.DataType type,
            @NotNull Boolean mandatory
    ) {
    }

    record ProductSpecsResponse(
            Long id,
            String name,
            String description,
            BigDecimal sellPrice,
            int quantity,
            Set<AttributeDto> attributes
    ) {
    }
}
