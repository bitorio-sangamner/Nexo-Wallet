package com.nexo_wallet.service.impl;

import com.nexo_wallet.entity.PlatformCurrencies;
import com.nexo_wallet.entity.UserOwnCurrencies;
import com.nexo_wallet.entity.Wallet;
import com.nexo_wallet.exceptions.UserOwnCurrenciesNotFoundException;
import com.nexo_wallet.repositories.PlatformCurrencyRepository;
import com.nexo_wallet.repositories.UserOwnCurrenciesRepository;
import com.nexo_wallet.repositories.WalletRepository;
import com.nexo_wallet.service.UserOwnCurrenciesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserOwnCurrenciesServiceImpl implements UserOwnCurrenciesService {

    @Autowired
    private PlatformCurrencyRepository platformCurrencyRepository;

    @Autowired
    private UserOwnCurrenciesRepository userOwnCurrenciesRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public List<UserOwnCurrencies> createUserOwnCurrencies(Wallet wallet) {

        log.info("inside createUserOwnCurrencies method.......");
        List<UserOwnCurrencies> userOwnCurrenciesList = new ArrayList<>();

        List<PlatformCurrencies> platformCurrencies = platformCurrencyRepository.findAll();

        for (PlatformCurrencies currency : platformCurrencies) {
            // Access individual currency object here
            // For example: currency.getName(), currency.getValue(), etc.
            log.info("currency name :"+currency.getCurrencyName());
            log.info("currency symbol :"+currency.getSymbol());

            UserOwnCurrencies userOwnCurrencies = UserOwnCurrencies.builder()
                    .currencyName(currency.getCurrencyName())
                    .symbol(currency.getSymbol())
                    .wallet(wallet)
                    .build();

            log.info("User own currency :"+userOwnCurrencies.getCurrencyName());
            log.info("User own currency symbol :"+userOwnCurrencies.getSymbol());

            userOwnCurrenciesList.add(userOwnCurrencies);
        }


        if(userOwnCurrenciesList!=null) {
            for (UserOwnCurrencies userOwnCurrency : userOwnCurrenciesList) {
                log.info("userOwnCurrencyList currency name :" + userOwnCurrency.getCurrencyName());
                log.info("userOwnCurrencyList currency name :" + userOwnCurrency.getSymbol());
            }
        }
        else {
            log.info("list is null");
        }
        return userOwnCurrenciesList;

    }

    @Override
    public List<UserOwnCurrencies> getUserOwnCurrenciesByEmail(String email) {
        // You may consider additional validation for the email parameter

        try {
            List<UserOwnCurrencies> userOwnCurrenciesList = userOwnCurrenciesRepository.findByUserEmail(email);
            if (userOwnCurrenciesList.isEmpty()) {
                throw new UserOwnCurrenciesNotFoundException("User own currencies not found for email: " + email);
            }
            return userOwnCurrenciesList;
        } catch (Exception e) {
            // Log the exception
            // You can rethrow or handle the exception based on your application's requirements
            throw new RuntimeException("Error retrieving user own currencies for email: " + email, e);
        }
    }

    @Override
    public UserOwnCurrencies getUserCurrencyByUserEmailAndCurrencyName(String userEmail, String currencyName) {
        return userOwnCurrenciesRepository.findByUserEmailAndCurrencyName(userEmail, currencyName);
    }

}
