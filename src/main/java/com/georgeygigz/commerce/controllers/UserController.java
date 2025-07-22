package com.georgeygigz.commerce.controllers;


import com.georgeygigz.commerce.dtos.*;
import com.georgeygigz.commerce.exceptions.EmailExistException;
import com.georgeygigz.commerce.exceptions.UnauthorizedException;
import com.georgeygigz.commerce.exceptions.UserNotFoundException;
import com.georgeygigz.commerce.mappers.UserMapper;
import com.georgeygigz.commerce.repositories.UserRepository;
import com.georgeygigz.commerce.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public Iterable<UserDto> getAllUsers(@RequestParam(required = false, defaultValue = "", name="sort") String sort) {
        if (!Set.of("name", "email").contains(sort))
            sort = "name";

        return userService.getAllUsers(sort);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto>  getUser(@PathVariable Long userId) {
        var userDto = userService.getUser(userId);
        return ResponseEntity.ok(userDto);
    }


    @PostMapping("/")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder
    ) {

        if(userRepository.existsByEmail(request.getEmail()))
            throw new EmailExistException();
        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(userMapper.toDto(user));

    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto>  updateUser(
            @PathVariable Long userId,
            @RequestBody UpdateUserRequest request
    ){
        var user = userService.updateUser(request, userId);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public  ResponseEntity<Void> deleteUser(@PathVariable Long userId){

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/change-password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long userId,
            @RequestBody ChangePasswordRequest request
    ){
        userService.updatePassword(request,userId);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<Map<String, String>> handleEmailExist(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Email already in use"));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User does not exist"));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorized(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
    }

}

