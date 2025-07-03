package com.georgeygigz.commerce.repositories;

import com.georgeygigz.commerce.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}