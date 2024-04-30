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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String walletAddress;
   private String currencyName;
    private String currencyAbbr;
    private String blockchainNetwork;
    private Long userId;
    private String userEmail;

    @OneToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

}
