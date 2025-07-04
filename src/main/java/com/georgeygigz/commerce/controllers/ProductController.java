package com.georgeygigz.commerce.controllers;

import com.georgeygigz.commerce.dtos.ProductDto;
import com.georgeygigz.commerce.entities.Category;
import com.georgeygigz.commerce.entities.Product;
import com.georgeygigz.commerce.mappers.ProductMapper;
import com.georgeygigz.commerce.repositories.CategoryRepository;
import com.georgeygigz.commerce.repositories.ProductRepository;
import com.georgeygigz.commerce.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @GetMapping("/")
    public Iterable<ProductDto> getAllProducts(@RequestParam(name = "categoryId", required = false) Byte categoryId) {
        List<Product> products;
        if (categoryId != null)
            products = productRepository.findByCategoryId(categoryId);
        else
            products = productRepository.findAllWithCategory();
        return products.stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto>  getProduct(@PathVariable Long productId) {
        var product = productRepository.findById(productId).orElse(null);
        if (product == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(productMapper.toDto(product));

    }

    @PostMapping("/")
    public ResponseEntity<ProductDto> addProduct(
            @RequestBody  ProductDto productDto,
            UriComponentsBuilder uriBuilder
            ) {
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null)
            return ResponseEntity.notFound().build();

        var product = productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);

        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(productMapper.toDto(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto){
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        var product = productRepository.findById(id).orElse(null);
        if (product == null)
            return ResponseEntity.notFound().build();


        if (category== null)
            return ResponseEntity.badRequest().build();

        productMapper.update(productDto,product);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());
        return ResponseEntity.ok(productDto);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        var product = productRepository.findById(id).orElse(null);
        if (product == null)
            return ResponseEntity.notFound().build();

        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
