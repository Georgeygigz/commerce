package com.georgeygigz.commerce.mappers;

import com.georgeygigz.commerce.dtos.ProductDto;
import com.georgeygigz.commerce.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto (Product product);

}
