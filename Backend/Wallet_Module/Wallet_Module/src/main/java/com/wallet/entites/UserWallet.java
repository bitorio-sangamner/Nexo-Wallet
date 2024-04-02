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
public class UserWallet {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private long userId;
    private String userName;
    private String currencyName;
    private String symbol;
    private BigDecimal availableBalance;
    private BigDecimal lockedBalance;
    private  BigDecimal currentPrice;

    public UserWallet(Long userId, String userName, String currencyName, String symbol, BigDecimal availableBalance, BigDecimal lockedBalance, BigDecimal currentPrice) {

        this.userId=userId;
        this.userName=userName;
        this.currencyName=currencyName;
        this.symbol=symbol;
        this.availableBalance=availableBalance;
        this.lockedBalance=lockedBalance;
        this.currentPrice=currentPrice;
    }
}
