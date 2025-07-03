package com.georgeygigz.commerce.repositories;

import com.georgeygigz.commerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}