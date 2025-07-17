package com.georgeygigz.commerce.controllers;

import com.georgeygigz.commerce.dtos.AddItemToCartRequest;
import com.georgeygigz.commerce.dtos.CartDto;
import com.georgeygigz.commerce.dtos.CartItemDto;
import com.georgeygigz.commerce.dtos.UpdateCartItemRequest;
import com.georgeygigz.commerce.entities.Cart;
import com.georgeygigz.commerce.entities.CartItem;
import com.georgeygigz.commerce.mappers.CartMapper;
import com.georgeygigz.commerce.repositories.CartRepository;
import com.georgeygigz.commerce.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Value;
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
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @PostMapping("/")
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ){
        var cart =  new Cart();
        cartRepository.save(cart);
        var cartDto = cartMapper.toDto(cart);

        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);

    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddItemToCartRequest request
    ){
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart==null){
            return ResponseEntity.notFound().build();
        }

        var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product==null)
            return ResponseEntity.badRequest().build();
        var cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);
        if (cartItem !=null){
            cartItem.setQuantity(cartItem.getQuantity() +1);
        }else{
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cart.getItems().add(cartItem);
        }
        cartRepository.save(cart);
        var cartItemDto = cartMapper.toDto(cartItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);

    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart ==null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(cartMapper.toDto(cart));

    }

    @PutMapping("/{cartId}/items/{productID}")
    public ResponseEntity<?> updateItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productID") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ){

        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart ==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart was not found")
            );

        var cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (cartItem == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Product was not found in the cart")
            );
        cartItem.setQuantity(cartItem.getQuantity() +1);
        cartRepository.save(cart);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartMapper.toDto(cart));


    }

}
