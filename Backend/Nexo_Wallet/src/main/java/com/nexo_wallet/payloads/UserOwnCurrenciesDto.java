package com.nexo_wallet.payloads;

import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserOwnCurrenciesDto {

    private int userCurrencyId;
    private String currencyName;
    private String symbol;
    private BigDecimal marketPrice;
    private BigDecimal totalAmount;
    private BigDecimal spotBalance;
    private BigDecimal lockBalance;
    private BigDecimal availableBalance;

}
