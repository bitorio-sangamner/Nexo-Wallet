package com.wallet.payloads;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletBalanceDetailsDto {
    private BigDecimal fundingWallet;
    private BigDecimal tradingWallet;

}
