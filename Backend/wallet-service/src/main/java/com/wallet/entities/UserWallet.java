package com.wallet.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "wallet_address")
    private String walletAddress;


    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

//    @OneToOne
//    @JoinColumn(name = "user_wallet_balance_id", referencedColumnName = "id")
//    private UserWalletBalance userWalletBalance;
}
