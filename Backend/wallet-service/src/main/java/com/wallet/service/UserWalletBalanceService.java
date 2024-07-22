package com.wallet.service;

import com.wallet.entities.Currency;
import com.wallet.entities.UserWalletBalance;
import com.wallet.payloads.UserWalletBalanceDto;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public interface UserWalletBalanceService {

     String createUserWalletBalance(Long userId,String email,Currency currency);
     //UserWalletBalance createUserWalletBalance();

     UserWalletBalanceDto getUserCurrencyByUserEmailAndCurrencyName(String email, String currencyName);

     UserWalletBalance getWalletBalance(String email, String currencyAbb);

}
