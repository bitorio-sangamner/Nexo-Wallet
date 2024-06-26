package com.authentication.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_cred")
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(unique = true, name = "email")
    @NotNull
    @Email
    String email;

    @Column(name = "password")
    @Size(min = 6, max = 25)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,16}$")
    @NotNull
    String password;

    @Column(name = "pin")
    @Digits(integer = 6, fraction = 0)
    int pin;

    @Column(name = "roles")
    String roles;

    @Column(name = "is-verified")
    boolean isVerified;

    @Column(name = "verify-token")
    String verifyToken;

    @Column(name = "verify-email-token-gen-time")
    long verifyEmailTokenGenerationTime;

    @Column(name = "verify-email-state-active")
    boolean verifyEmailStateActive;

    @Column(name = "reset-password-state-active")
    boolean isResetPasswordStateActive;

    @Column(name = "reset-password-link-gen-time")
    long resetPasswordLinkGenerationTime;

    @Column(name = "is-logged-in")
    @Builder.Default
    boolean isLoggedIn = false;
}
