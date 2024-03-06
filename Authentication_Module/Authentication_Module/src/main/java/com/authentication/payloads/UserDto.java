package com.authentication.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Getter
@Setter
public class UserDto {


    private int id;
    private String fullname;
    private String email;
    private String password;
    private String pin;
}
