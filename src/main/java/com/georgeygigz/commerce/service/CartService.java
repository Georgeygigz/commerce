package com.georgeygigz.commerce.service;

import com.georgeygigz.commerce.dtos.CartDto;
import com.georgeygigz.commerce.dtos.CartItemDto;
import com.georgeygigz.commerce.entities.Cart;
import com.georgeygigz.commerce.exceptions.CartNotFoundException;
import com.georgeygigz.commerce.exceptions.ProductNotFoundException;
import com.georgeygigz.commerce.mappers.CartMapper;
import com.georgeygigz.commerce.repositories.CartRepository;
import com.georgeygigz.commerce.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;


    public CartDto createCart(){
        var cart = new Cart();
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productId){
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart==null){
            throw new CartNotFoundException();
        }

        var product = productRepository.findById(productId).orElse(null);
        if (product==null)
            throw new ProductNotFoundException();

        var cartItem = cart.addItem(product);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart ==null)
            throw new CartNotFoundException();
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateItem(UUID cartId, Long productId, Integer quantity){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart ==null)
            throw new CartNotFoundException();
        var cartItem = cart.getItem(productId);

        if (cartItem == null)
            throw new ProductNotFoundException();

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public void deleteItem(UUID cartId, Long productId){
        var cart =  cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public CartDto clearCart(UUID cartId){
        var cart =  cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null)
            throw new CartNotFoundException();
        cart.clear();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }
}
