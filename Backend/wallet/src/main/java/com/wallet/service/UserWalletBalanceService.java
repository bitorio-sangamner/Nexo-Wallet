package com.wallet.service;

import com.wallet.payloads.UserWalletBalanceDto;
import org.springframework.stereotype.Component;

@Component
public interface UserWalletBalanceService {

     void createUserWalletBalance(Long id,String email);

     UserWalletBalanceDto getUserCurrencyByUserEmailAndCurrencyName(String email, String currencyName);
}
