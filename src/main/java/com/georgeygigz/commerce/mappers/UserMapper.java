package com.georgeygigz.commerce.mappers;

import com.georgeygigz.commerce.dtos.RegisterUserRequest;
import com.georgeygigz.commerce.dtos.UpdateUserRequest;
import com.georgeygigz.commerce.dtos.UserDto;
import com.georgeygigz.commerce.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
