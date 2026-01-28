package eu.martin.store.products;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RequestMapping("${app.product-path}")
@RestController
class ProductController {
    private final ProductService service;

    @Value("${app.product-path}")
    private String productPath;

    /**
     * Creates new product in the database.
     * @param dto ProductRegisterDto
     * @return ProductResponse
     */

    @Operation(summary = "Creates new product in the database.")
    @PostMapping
    ResponseEntity<ProductResponse> registerNewProduct(@RequestBody @Valid ProductRegisterDto dto, UriComponentsBuilder uriBuilder) { // test passed
        var productResponse = service.registerNewProduct(dto);
        var uri = uriBuilder.path(productPath + "/{id}").buildAndExpand(productResponse.id()).toUri();
        return ResponseEntity.created(uri).body(productResponse);
    }


    /**
     * Saves buy history/logs (Increases quantity of product, adds buy price and supplier)
     *
     * @param id  Integer
     * @param dto ProductBuyRequest
     * @return ProductBuyResponse
     */
    @Operation(summary = "Saves buy logs into the database (Increases quantity of product, adds buy price and supplier)")
    @PostMapping("/{id}")
    ResponseEntity<ProductBuyResponse> buyProduct(@PathVariable Integer id, @RequestBody @Valid ProductBuyRequest dto) { // test passed
        return ResponseEntity.ok(service.buyProduct(id, dto));
    }

    @Operation(summary = "Returns product by ID.")
    @GetMapping("/{id}")
    ResponseEntity<ProductResponse> getProduct(@PathVariable Integer id) { // test passed
        return ResponseEntity.ok(service.getProductById(id));
    }

    @Operation(summary = "Updates product by ID.")
    @PutMapping("/{id}")
    ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer id, @RequestBody @Valid ProductUpdateDto dto) { // test passed
        return ResponseEntity.ok(service.update(id, dto));
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
}
