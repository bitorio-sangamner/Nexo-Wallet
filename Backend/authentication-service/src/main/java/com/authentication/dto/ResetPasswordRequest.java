package com.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequest(
        @Email(message = "The email should be of email format.") String email,
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,25}$",
                message = "The password should contain at least one Uppercase, one lowercase letter, one digit, one special character and between length 8 and 25. ")
        String oldPassword,
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,25}$",
                message = "The password should contain at least one Uppercase, one lowercase letter, one digit, one special character and between length 8 and 25. ")
        String newPassword) {}
