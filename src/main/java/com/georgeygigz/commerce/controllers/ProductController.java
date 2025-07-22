package com.georgeygigz.commerce.controllers;

import com.georgeygigz.commerce.dtos.ProductDto;
import com.georgeygigz.commerce.mappers.ProductMapper;
import com.georgeygigz.commerce.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductMapper productMapper;
    private final ProductService productService;

    @GetMapping("/")
    public Iterable<ProductDto> getAllProducts(@RequestParam(name = "categoryId", required = false) Byte categoryId) {
        var products = productService.getAllProducts(categoryId);
        return products.stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto>  getProduct(@PathVariable Long productId) {
        var productDto = productService.getProduct(productId);
        return ResponseEntity.ok(productDto);

    }

    @PostMapping("/")
    public ResponseEntity<ProductDto> addProduct(
            @RequestBody  ProductDto request,
            UriComponentsBuilder uriBuilder
            ) {

        var product = productService.addProduct(request);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(productMapper.toDto(product));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId, @RequestBody ProductDto productDto){
        var pDto = productService.updateProduct(productDto,productId);
        return ResponseEntity.ok(pDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

}
