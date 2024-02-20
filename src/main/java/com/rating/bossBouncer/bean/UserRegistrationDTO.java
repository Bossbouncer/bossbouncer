package com.rating.bossBouncer.bean;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDTO {

    private String firstName;
    private String lastName;
    @NotEmpty(message = "Username is required")
    private String userName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
