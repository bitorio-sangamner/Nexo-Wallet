package com.nexo_wallet.payloads;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private LocalDate createdOn;
    private LocalDate updatedOn;

}
