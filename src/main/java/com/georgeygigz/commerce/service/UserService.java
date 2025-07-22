package com.georgeygigz.commerce.service;

import com.georgeygigz.commerce.dtos.ChangePasswordRequest;
import com.georgeygigz.commerce.dtos.RegisterUserRequest;
import com.georgeygigz.commerce.dtos.UpdateUserRequest;
import com.georgeygigz.commerce.dtos.UserDto;
import com.georgeygigz.commerce.entities.User;
import com.georgeygigz.commerce.exceptions.EmailExistException;
import com.georgeygigz.commerce.exceptions.UnauthorizedException;
import com.georgeygigz.commerce.exceptions.UserNotFoundException;
import com.georgeygigz.commerce.mappers.UserMapper;
import com.georgeygigz.commerce.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers(String sort){
        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUser(Long userId){
        var user = userRepository.findById(userId).orElse(null);
        if (user == null){
            throw new UserNotFoundException();
        }

        return userMapper.toDto(user);
    }

    public UserDto registerUser(RegisterUserRequest request){
        if(userRepository.existsByEmail(request.getEmail()))
            throw new EmailExistException();
        var user = userMapper.toEntity(request);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserDto updateUser(UpdateUserRequest request, Long userId){
        var user = userRepository.findById(userId).orElse(null);
        if (user == null)
            throw new UserNotFoundException();

        userMapper.update(request, user);
        userRepository.save(user);
        return userMapper.toDto(user);

    }

    public void updatePassword(ChangePasswordRequest request, Long userId){
        var user = userRepository.findById(userId).orElse(null);
        if(user == null)
            throw new UserNotFoundException();
        if (!user.getPassword().equals(request.getOldPassword()))
            throw new UnauthorizedException();
        user.setPassword(request.getNewPassword());
        userRepository.save(user);

    }

}
