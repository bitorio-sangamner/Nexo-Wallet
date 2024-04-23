package com.wallet.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class CurrencyWalletDto {


    private String currencyName;
    private String symbol;
    private BigDecimal availableBalance;
    private BigDecimal lockedBalance;
    private BigDecimal currentPrice;
}
