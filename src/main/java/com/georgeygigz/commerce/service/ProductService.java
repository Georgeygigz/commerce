package com.georgeygigz.commerce.service;

import com.georgeygigz.commerce.dtos.ProductDto;
import com.georgeygigz.commerce.entities.Product;
import com.georgeygigz.commerce.exceptions.CategoryNotFoundException;
import com.georgeygigz.commerce.exceptions.ProductNotFoundException;
import com.georgeygigz.commerce.mappers.ProductMapper;
import com.georgeygigz.commerce.repositories.CartRepository;
import com.georgeygigz.commerce.repositories.CategoryRepository;
import com.georgeygigz.commerce.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public List<Product> getAllProducts(Byte categoryId){
        List<Product> products;
        if (categoryId != null)
            products = productRepository.findByCategoryId(categoryId);
        else
            products = productRepository.findAllWithCategory();

        return products;
    }

    public ProductDto getProduct(Long productId){
        var product = productRepository.findById(productId).orElse(null);
        if (product == null)
            throw new ProductNotFoundException();
        return productMapper.toDto(product);
    }

    public Product addProduct(ProductDto productDto){
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null)
            throw new CategoryNotFoundException();

        var product = productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);
        return product;
    }

    public ProductDto updateProduct(ProductDto productDto, Long productId){
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        var product = productRepository.findById(productId).orElse(null);
        if (product == null)
            throw new ProductNotFoundException();


        if (category== null)
            throw new CategoryNotFoundException();

        productMapper.update(productDto,product);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());
        return productDto;
    }

    public void deleteProduct(Long productId){
        var product = productRepository.findById(productId).orElse(null);
        if (product == null)
            throw new ProductNotFoundException();

        productRepository.deleteById(productId);
    }
}
