package com.wallet.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Entity
@NoArgsConstructor
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
}
