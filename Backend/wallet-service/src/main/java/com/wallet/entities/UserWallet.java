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

//    @Column(name = "blockchain_network")
//    private String blockchainNetwork;
//
//    @Column(name = "currency_abbr")
//    private String currencyAbbr;
//
//    @Column(name = "currency_name")
//    private String currencyName;

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
//    @JoinColumn(name = "currency_id", nullable = false)
//    private Currency currency;

}
