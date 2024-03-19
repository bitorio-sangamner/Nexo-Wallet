package com.authentication.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCoinsDto {

    private Long id;
    private long userId;

    private String name;
    private String symbol;
    private BigDecimal availableBalance;
    private BigDecimal lockedBalance;
    private static BigDecimal currentPrice;
}
