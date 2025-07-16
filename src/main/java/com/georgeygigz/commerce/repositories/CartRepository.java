package com.georgeygigz.commerce.repositories;

import com.georgeygigz.commerce.entities.Cart;
import com.georgeygigz.commerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository <Cart, UUID> {
}
