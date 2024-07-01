package com.authentication.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

/**
 * This record is used by the user for log in to the system.
 *
 * @param email    Email address of the user registered in the system.
 * @param password Password for the account registered.
 * @param pin      Numeric pin for the account registered.
 * @author rsmalani
 */
public record AuthRequest(
        @Email(message = "The email should be of email format.") String email,
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,25}$",
                 message = "The password should contain at least one Uppercase, one lowercase letter, one digit, one special character and between length 8 and 25. ")
        String password,
        @Digits(integer = 6, fraction = 0, message = "The pin must contain only 6 digits.") int pin) {}