package com.example.miniproject.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
@Builder
public record SignUpRequest(
        @NotBlank(message = "Full name cannot be blank")
        @Size(max = 100, message = "Full name cannot exceed 100 characters")
        String fullName,

        @NotBlank(message = "Phone number cannot be blank")
        String phoneNumber,

        @NotBlank(message = "Email cannot be blank")
        @Size(max = 320, message = "Email cannot exceed 320 characters")
        @Email(message = "Invalid email format")
        String email,
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 12, message = "Password must be between 6 and 12 characters long")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).+$",
                message = "Password must contain at least one letter and one number"
        )
        String password,
        @NotBlank(message = "Confirm password cannot be blank")
        String confirmPassword
) {
}