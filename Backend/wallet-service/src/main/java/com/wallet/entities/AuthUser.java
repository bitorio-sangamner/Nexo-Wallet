package com.wallet.entities;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class AuthUser {

    int id;
    String email;
    String password;
    String roles;


}
