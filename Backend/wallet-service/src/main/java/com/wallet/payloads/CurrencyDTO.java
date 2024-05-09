package com.wallet.payloads;

import com.wallet.enums.CurrencyWalletProvider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyDTO {
    private String currencyName;
    private String currencyAbb;
    private String blockchainNetwork;
    private CurrencyWalletProvider currencyWalletProvider;
}
