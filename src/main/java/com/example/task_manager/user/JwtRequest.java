package com.example.task_manager.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JwtRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100)
    private String password;

    @NotBlank(message = "Email is required")
    @Email
    private String email;
}
