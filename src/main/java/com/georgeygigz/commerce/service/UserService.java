package com.georgeygigz.commerce.service;

import com.georgeygigz.commerce.dtos.*;
import com.georgeygigz.commerce.exceptions.EmailExistException;
import com.georgeygigz.commerce.exceptions.UnauthorizedException;
import com.georgeygigz.commerce.exceptions.UserNotFoundException;
import com.georgeygigz.commerce.mappers.UserMapper;
import com.georgeygigz.commerce.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(user.getEmail(), user.getPassword(), Collections.emptyList());

    }

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
