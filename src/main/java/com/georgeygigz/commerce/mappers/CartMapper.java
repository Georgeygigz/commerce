package com.georgeygigz.commerce.mappers;

import com.georgeygigz.commerce.dtos.CartDto;
import com.georgeygigz.commerce.dtos.CartItemDto;
import com.georgeygigz.commerce.entities.Cart;
import com.georgeygigz.commerce.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target="totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
