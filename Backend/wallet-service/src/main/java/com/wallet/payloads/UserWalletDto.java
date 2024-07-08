package com.wallet.payloads;

import com.wallet.entities.Currency;
import com.wallet.entities.UserWalletBalance;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWalletDto {

    String walletAddress;
//    String currencyName;
//    String currencyAbbr;
//    String blockchainNetwork;
    Long userId;
    String email;
    private Currency currency;
    private UserWalletBalance userWalletBalance;
}
