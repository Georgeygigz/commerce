package com.georgeygigz.commerce.controllers;

import com.georgeygigz.commerce.dtos.ProductDto;
import com.georgeygigz.commerce.entities.Product;
import com.georgeygigz.commerce.mappers.ProductMapper;
import com.georgeygigz.commerce.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

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

}
