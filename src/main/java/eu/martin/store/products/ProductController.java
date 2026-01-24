package eu.martin.store.products;

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

    @PostMapping
    ResponseEntity<ProductResponse> registerNewProduct(@RequestBody @Valid ProductRegisterDto dto, UriComponentsBuilder uriBuilder) { // test passed
        var productResponse = service.registerNewProduct(dto);
        var uri = uriBuilder.path(productPath + "/{id}").buildAndExpand(productResponse.id()).toUri();
        return ResponseEntity.created(uri).body(productResponse);
    }


    /**
     * Stores buy history/logs (Increases quantity of product, adds buy price and supplier)
     *
     * @param id  Integer
     * @param dto ProductBuyRequest
     * @return ProductBuyResponse
     */
    @PostMapping("/{id}")
    ResponseEntity<ProductBuyResponse> buyProduct(@PathVariable Integer id, @RequestBody @Valid ProductBuyRequest dto) { // test passed
        return ResponseEntity.ok(service.buyProduct(id, dto));
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductResponse> getProduct(@PathVariable Integer id) { // test passed
        return ResponseEntity.ok(service.getProductById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer id, @RequestBody @Valid ProductUpdateDto dto) { // test passed
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping
    ResponseEntity<Page<Product>> getProducts(@RequestParam int page, @RequestParam int size) { // test passed
        return ResponseEntity.ok(service.getProtuctsPage(page, size));
    }

    @GetMapping("/{id}/logs")
    ResponseEntity<BuyLogResponse> getProductBuyLogs(@PathVariable Integer id) { // test passed
        return ResponseEntity.ok(service.getProductBuyLogs(id));
    }
}
