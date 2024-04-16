package com.wallet.service;

import com.wallet.payloads.CurrencyWalletDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CurrencyWalletService {
//
//    List<UserWalletDto> getAllCoinsOfUser();
//    public void updateCoinPrices();
//
//    public void createUserWallet(Long userId, String userName);
    public List<CurrencyWalletDto> getCurrencyHeldByUser(String userName);
//
    public CurrencyWalletDto searchCoin(String userName, String currency);


}
