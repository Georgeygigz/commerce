package com.georgeygigz.commerce.dtos;


import lombok.Data;

@Data
public class RegisterUserRequest {
    private String email;
    private String password;
    private String name;
}
