package com.georgeygigz.commerce.controllers;

import com.georgeygigz.commerce.dtos.AddItemToCartRequest;
import com.georgeygigz.commerce.dtos.CartDto;
import com.georgeygigz.commerce.dtos.CartItemDto;
import com.georgeygigz.commerce.dtos.UpdateCartItemRequest;
import com.georgeygigz.commerce.exceptions.CartNotFoundException;
import com.georgeygigz.commerce.exceptions.ProductNotFoundException;
import com.georgeygigz.commerce.service.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping("/")
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ){

        var cartDto = cartService.createCart();
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);

    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddItemToCartRequest request
    ){

        var cartItemDto = cartService.addToCart(cartId, request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);

    }

    @GetMapping("/{cartId}")
    public CartDto getCart(@PathVariable UUID cartId){
        return cartService.getCart(cartId);

    }

    @PutMapping("/{cartId}/items/{productID}")
    public CartItemDto updateItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productID") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ){

        return cartService.updateItem(cartId,productId, request.getQuantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeFromCart(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId
    ){
        cartService.deleteItem(cartId, productId);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable("cartId") UUID cartId){
        var cartDto = cartService.clearCart(cartId);
        return ResponseEntity.status(HttpStatus.OK).body(cartDto);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public  ResponseEntity<Map<String, String>> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found in the cart"));
    }

}
