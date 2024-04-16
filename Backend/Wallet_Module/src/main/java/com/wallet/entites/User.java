package com.wallet.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(mappedBy = "user",targetEntity = CurrencyWallet.class,cascade= CascadeType.ALL)
    private List<CurrencyWallet> currencyWallets;

    @OneToMany(mappedBy = "user",targetEntity = Transaction.class,cascade = CascadeType.ALL)
    private List<Transaction> transactionList;

    @PrePersist
    //This method will be executed just before the entity is persisted.
    protected void onCreate() {
        createdOn = LocalDate.from(LocalDateTime.now());
    }

}
