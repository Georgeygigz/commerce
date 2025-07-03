package com.georgeygigz.commerce.repositories;

import com.georgeygigz.commerce.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}