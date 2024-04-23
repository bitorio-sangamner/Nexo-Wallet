package com.wallet.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CurrencyWallet {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String currencyName;
    private String symbol;
    private BigDecimal availableBalance;
    private BigDecimal lockedBalance;
    private  BigDecimal currentPrice;

    @ManyToOne
    @JoinColumn(name = "userEmail_fk", referencedColumnName = "email")
    private User user;

    public CurrencyWallet(String currencyName, String symbol, BigDecimal availableBalance, BigDecimal lockedBalance, BigDecimal currentPrice,User user) {
        this.currencyName = currencyName;
        this.symbol = symbol;
        this.availableBalance = availableBalance;
        this.lockedBalance = lockedBalance;
        this.currentPrice = currentPrice;
        this.user=user;
    }

}
