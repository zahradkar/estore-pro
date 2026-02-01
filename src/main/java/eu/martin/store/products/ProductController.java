package eu.martin.store.products;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RequestMapping("${app.product-path}")
@RestController
class ProductController {
    private final ProductService service;

    @Value("${app.product-path}")
    private String productPath;

    @Operation(summary = "Registers new product (Creates product with attributes).")
    @PostMapping
    ResponseEntity<ProductWithAttribsDto> registerProductWithAttributes(@RequestBody @Valid ProductController.ProductWithAttribsDto dto, UriComponentsBuilder uriBuilder) {
        var result = service.createProductWithAttributes(dto);

        var uri = uriBuilder.path(productPath + "/{id}").buildAndExpand(result.id).toUri();

        return ResponseEntity.created(uri).body(result);
    }

    @Operation(summary = "Saves buy logs.")
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
        return ResponseEntity.ok(service.updateProduct(dto));
    }

    @Operation(summary = "Returns buy logs of given product.")
    @GetMapping("/{id}/logs")
    ResponseEntity<BuyLogResponse> getProductBuyLogs(@PathVariable Integer id) { // test passed
        return ResponseEntity.ok(service.getProductBuyLogs(id));
    }

    @Operation(summary = "Returns sorted products by specification.")
    @GetMapping("/specifications")
    ResponseEntity<List<ProductSpecsResponse>> getSortedProductsBySpecification(
            @RequestParam @Min(0) @Max(Short.MAX_VALUE) Short pageNumber,
            @RequestParam @Min(5) @Max(50) Short pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String attributeName,
            @RequestParam(required = false) String categoryName,
            @Parameter(description = "[id, name, description, sellPrice, quantity, measureUnit]")
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) boolean descending) { // test passed

        return ResponseEntity.ok(service.getSortedProductsBySpecification(pageNumber, pageSize, name, minPrice, maxPrice, attributeName, categoryName, sortBy, descending));
    }

    @Operation(summary = "Creates category.")
    @PostMapping("/category")
    ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryDto dto, UriComponentsBuilder uriBuilder) {
        var result = service.createCategory(dto);

        var uri = uriBuilder.path(productPath + "/category/{id}").buildAndExpand(result.id).toUri();

        return ResponseEntity.created(uri).body(result);
    }

    @Operation(summary = "Returns category by ID.")
    @GetMapping("/category/{id}")
    ResponseEntity<CategoryDto> getCategory(@PathVariable @Min(1) @Max(1_000) Short id) {
        return ResponseEntity.ok(service.getCategory(id));
    }

    @Operation(summary = "Updates category by ID.")
    @PutMapping("/category")
    ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto dto) {
        return ResponseEntity.ok(service.updateCategory(dto));
    }

    @Operation(summary = "Saves categorySummaries (Accepts a JSON file structured as a tree of categorySummaries).")
    @PostMapping(value = "/categories/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> importCategories(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty())
            return ResponseEntity.badRequest().body("File cannot be empty");

        service.importCategories(file);

        return ResponseEntity.ok("Categories imported successfully");
    }

    record ProductWithAttribsDto(
            Integer id,
            @NotBlank String name,
            String description,
            @NotNull BigDecimal sellPrice,
            @NotNull Integer quantity,
            @NotNull MeasureUnit measureUnit,
            Set<@Valid CategorySummary> categorySummaries,
            Set<@Valid AttributeDto> attributeDtos
    ) {
        record CategorySummary(
           @NotNull @Min(1) Short id,
           String name
        ) {
        }
    }

    record AttributeDto(
            @NotBlank String name,
            String value,
            @NotNull Attribute.DataType type,
            @NotNull Boolean mandatory
    ) {
    }

    record ProductBuyRequest(
            @NotNull @Min(1) Short quantity,
            @NotNull BigDecimal buyPrice,
            String supplier
    ) {
    }

    record ProductBuyResponse(
            int id,
            String name,
            short newQuantity,
            BigDecimal lastBuyPrice,
            String lastSupplier,
            LocalDateTime timestamp
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

    record CategoryDto(
            Short id,
            @NotBlank String name,
            String description,
            Category parent
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record CategoryImportDto(
            String name,
            String description,
            List<CategoryImportDto> children
    ) {
    }
}
