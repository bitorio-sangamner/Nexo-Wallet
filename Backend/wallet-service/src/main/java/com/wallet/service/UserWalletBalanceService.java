package com.wallet.service;

import com.wallet.entities.Currency;
import com.wallet.payloads.UserWalletBalanceDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserWalletBalanceService {

     //void createUserWalletBalance(Long id,String email);
     void createUserWalletBalance(Long userId, String email, Currency currency);

     UserWalletBalanceDto getUserCurrencyByUserEmailAndCurrencyName(String email, String currencyName);

     List<UserWalletBalanceDto> getWalletBalance(String email);

}
