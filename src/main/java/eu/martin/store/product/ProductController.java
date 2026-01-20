package eu.martin.store.product;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RequestMapping("/api/products")
@RestController
record ProductController(ProductService service) {

    @PostMapping
    ResponseEntity<ProductResponse> registerNewProduct(@RequestBody @Valid ProductRegisterDto dto, UriComponentsBuilder uriBuilder) {
        var productResponse = service.registerNewProduct(dto);
        var uri = uriBuilder.path("/api/products/{id}").buildAndExpand(productResponse.id()).toUri();
        return ResponseEntity.created(uri).body(productResponse);
    }


    /**
     * if increases quantity of product. If product is not stored in the database, it creates it of given quantity.
     *
     * @param id  Integer
     * @param dto ProductBuyRequest
     * @return ProductBuyResponse
     */
    @PostMapping("/{id}")
    ResponseEntity<ProductBuyResponse> buyProduct(@PathVariable Integer id, @RequestBody @Valid ProductBuyRequest dto) {
        return ResponseEntity.ok(service.buyProduct(id, dto));
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductResponse> getProduct(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(service.getProductById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer id, @RequestBody @Valid ProductUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping
    ResponseEntity<Page<Product>> getProducts(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(service.getProtuctsPage(page, size));
    }
}
