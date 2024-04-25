package com.nexo_wallet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlatformCurrencies {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int currencyId;

    private String currencyName;
    private String symbol;
    private BigDecimal marketPrice;
}
