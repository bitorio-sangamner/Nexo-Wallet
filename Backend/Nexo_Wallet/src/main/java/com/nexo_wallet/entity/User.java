package com.nexo_wallet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int userId;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private LocalDate createdOn;
    private LocalDate updatedOn;

    @OneToOne(mappedBy = "user", targetEntity = Wallet.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Add this annotation to break circular reference
    private Wallet wallet;

    @PrePersist
    //This method will be executed just before the entity is persisted.
    protected void onCreate() {
        createdOn = LocalDate.from(LocalDateTime.now());
    }

}
