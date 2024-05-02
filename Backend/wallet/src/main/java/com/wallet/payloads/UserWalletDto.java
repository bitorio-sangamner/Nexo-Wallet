package com.wallet.payloads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWalletDto {

    String walletAddress;
    String currencyName;
    String currencyAbbr;
    String blockchainNetwork;
    Long userId;
    String email;
}
