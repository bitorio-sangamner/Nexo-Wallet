package com.wallet.payloads;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UserWalletBalanceDto {

    private Long userId;
    private String email;
    private BigDecimal fundingWallet;
    private BigDecimal tradingWallet;
    private String currencyName;
    private String currencyAbb;
}
