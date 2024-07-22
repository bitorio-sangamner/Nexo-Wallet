package com.wallet.payloads;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletDetailsDto {

    private String walletAddress;
    private String currencyAbb;

    private String blockChainNetwork;
}
