package com.georgeygigz.commerce.repositories;

import com.georgeygigz.commerce.entities.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE :categoryId = 0 OR p.category.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") @RequestParam(required = false, defaultValue = "0") int categoryId);


}