package eu.martin.store.product;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RequestMapping("/api/product")
@RestController
record ProductController(ProductService service) {

    @PostMapping("/new")
    ResponseEntity<ProductResponse> registerNewProduct(@RequestBody ProductRequest dto, UriComponentsBuilder uriBuilder) {
        var productResponse = service.registerNewProduct(dto);
        var uri = uriBuilder.path("/product/{id}").buildAndExpand(productResponse.id()).toUri();
        return ResponseEntity.created(uri).body(productResponse);
    }


    /**
     * if increases quantity of product. If product is not stored in the database, it creates it of given quantity.
     * @return
     */
    @PostMapping
    ResponseEntity<ProductResponse> buyProduct(@RequestBody ProductRequest dto) {
        return ResponseEntity.ok(service.buyProduct(dto));
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductResponse> getProduct(@PathVariable(name = "id") Integer id) {
        return null;
    }
}
