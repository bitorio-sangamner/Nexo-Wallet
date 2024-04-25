package com.nexo_wallet.service;

import com.nexo_wallet.entity.UserOwnCurrencies;
import com.nexo_wallet.entity.Wallet;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserOwnCurrenciesService {

    List<UserOwnCurrencies> createUserOwnCurrencies(Wallet wallet);
    List<UserOwnCurrencies> getUserOwnCurrenciesByEmail(String email);

    UserOwnCurrencies getUserCurrencyByUserEmailAndCurrencyName(String userEmail, String currencyName);
}
