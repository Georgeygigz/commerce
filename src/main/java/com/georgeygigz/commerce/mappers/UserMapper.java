package com.georgeygigz.commerce.mappers;

import com.georgeygigz.commerce.dtos.UserDto;
import com.georgeygigz.commerce.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
