package com.georgeygigz.commerce.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotNull(message = "Email required")
    @Email
    private String email;

    @NotNull(message = "Password required")
    private String password;
}
