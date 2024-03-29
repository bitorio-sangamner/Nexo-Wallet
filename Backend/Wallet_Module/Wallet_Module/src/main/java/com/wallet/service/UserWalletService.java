package com.wallet.service;

import com.wallet.payloads.UserWalletDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserWalletService {

    List<UserWalletDto> getAllCoinsOfUser();
    public void updateCoinPrices();

    public void createUserWallet(Long userId, String userName);
    public List<UserWalletDto> getCurrencyHeldByUser(Long userId);

    public UserWalletDto searchCoin(String userName,String currency);


}
