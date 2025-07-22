package com.georgeygigz.commerce.controllers;

import com.georgeygigz.commerce.dtos.LoginRequest;
import com.georgeygigz.commerce.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        return ResponseEntity.ok("");
    }

//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<Void> handleBadCredentialsException() {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }

}
