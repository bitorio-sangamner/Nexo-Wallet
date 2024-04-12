package com.authentication.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth_cred")
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(unique = true, name = "email")
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "pin")
    int pin;

    @Column(name = "roles")
    String roles;

    @Column(name = "is-verified")
    boolean isVerified;
}
