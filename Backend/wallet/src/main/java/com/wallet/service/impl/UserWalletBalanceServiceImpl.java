package com.wallet.service.impl;

import com.wallet.entities.Currency;
import com.wallet.entities.UserWallet;
import com.wallet.entities.UserWalletBalance;
import com.wallet.payloads.UserWalletBalanceDto;
import com.wallet.payloads.UserWalletDto;
import com.wallet.repositories.CurrencyRepository;
import com.wallet.repositories.UserWalletBalanceRepository;
import com.wallet.service.UserWalletBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class UserWalletBalanceServiceImpl implements UserWalletBalanceService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private UserWalletBalanceRepository userWalletBalanceRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public void createUserWalletBalance(Long id, String email) {
        try {
            List<Currency> currencies = currencyRepository.findAll();
            currencies.forEach(currency -> {
                System.out.println("Currency Name: " + currency.getCurrencyName());
                System.out.println("Currency Abbreviation: " + currency.getCurrencyAbb());

                UserWalletBalance userWalletBalance = UserWalletBalance.builder()
                        .userId(id)
                        .email(email)
                        .currencyName(currency.getCurrencyName())
                        .currencyAbb(currency.getCurrencyAbb())
                        .fundingWallet(BigDecimal.ZERO)
                        .tradingWallet(BigDecimal.ZERO)
                        .build();

                userWalletBalanceRepository.save(userWalletBalance);
            });
        } catch (Exception e) {
            // Log the exception
            log.error(String.valueOf(e));
            e.printStackTrace();
            throw new RuntimeException("Failed to create user wallet balance.", e);
        }
    }

    @Override
    public UserWalletBalanceDto getUserCurrencyByUserEmailAndCurrencyName(String email, String currencyName) {

        UserWalletBalance userWalletBalance=this.userWalletBalanceRepository.findByEmailAndCurrencyName(email,currencyName);
        return this.userWalletBalanceToDto(userWalletBalance);
    }



    public UserWalletBalanceDto userWalletBalanceToDto(UserWalletBalance walletBalance)
    {
        UserWalletBalanceDto walletBalanceDto=this.modelMapper.map(walletBalance, UserWalletBalanceDto.class);
        return walletBalanceDto;
    }

    public UserWalletBalance dtoToUserWalletBalance(UserWalletBalanceDto walletBalanceDtoDto)
    {
        UserWalletBalance walletBalance=this.modelMapper.map(walletBalanceDtoDto, UserWalletBalance.class);
        return walletBalance;
    }
}
