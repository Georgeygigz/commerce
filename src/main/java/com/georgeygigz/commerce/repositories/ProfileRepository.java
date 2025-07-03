package com.georgeygigz.commerce.repositories;

import com.georgeygigz.commerce.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}