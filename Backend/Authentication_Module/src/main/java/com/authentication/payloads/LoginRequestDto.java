package com.authentication.payloads;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDto {

    @Email(regexp="[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",message="Email address is not valid !!")
    @NotNull
    @NotBlank
    @NotEmpty(message="Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 8,max = 25,message="Password must be min of 8 chars and max of 25 chars !!")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,25}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be between 8 and 25 characters long."
    )
    private String Password;
}
