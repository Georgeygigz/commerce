package com.georgeygigz.commerce.dtos;

import com.georgeygigz.commerce.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Byte categoryId;

}
