package com.wallet.entities;

import com.wallet.enums.CurrencyWalletProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Currency {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    String currencyName;
    String currencyAbb;
    String blockchainNetwork;
    @Enumerated(value = EnumType.STRING)
    private CurrencyWalletProvider currencyWalletProvider;
}
