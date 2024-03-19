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
public class UserCoinsDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private long userId;

    private String name;
    private String symbol;
    private BigDecimal availableBalance;
    private BigDecimal lockedBalance;
    private static BigDecimal currentPrice;
}
