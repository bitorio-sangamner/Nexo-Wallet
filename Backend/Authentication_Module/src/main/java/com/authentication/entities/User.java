package com.authentication.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @Column(name="fullName" ,nullable = false,length=100)
    private String fullname;
    private String email;
    private String password;
    //private String pin;

    private boolean active;
    private String otp;
    private LocalDateTime otpGeneratedTime;

}
