package com.nexo_wallet.service;

import com.nexo_wallet.entity.Wallet;
import com.nexo_wallet.payloads.WalletDto;
import org.springframework.stereotype.Component;

@Component
public interface WalletService {

    WalletDto getWalletByEmailWithCurrencies(String userName);


}
