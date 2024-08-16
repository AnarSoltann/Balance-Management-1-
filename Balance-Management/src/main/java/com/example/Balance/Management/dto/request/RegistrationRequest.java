package com.example.Balance.Management.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @NotEmpty(message = "Username is required")
    String username;
    @NotEmpty(message = "First name is required")
    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String firstName;
    @NotEmpty(message = "Last name is required")
    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 3, max = 200, message = "Surname must be between 3 and 200 characters")
    private String lastName;
    @Email(message = "Email is invalid")
    @NotEmpty(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Email must be valid.Example: firstname-lastname@example.com  ")
    @NotBlank(message = "Email cannot be blank")
    private String email;
    @NotEmpty(message = "Password is required")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

}