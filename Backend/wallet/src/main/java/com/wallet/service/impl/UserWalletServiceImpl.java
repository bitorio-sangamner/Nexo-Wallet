package com.wallet.service.impl;

import com.wallet.entities.Currency;
import com.wallet.entities.UserWallet;
import com.wallet.payloads.UserWalletDto;
import com.wallet.repositories.CurrencyRepository;
import com.wallet.repositories.UserWalletRepository;
import com.wallet.service.UserWalletService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserWalletServiceImpl implements UserWalletService {

    @Autowired
    private UserWalletRepository userWalletRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public void createWallet(Long userId, String email) {
        try {
            List<Currency> currencies = currencyRepository.findAll();
            currencies.forEach(currency -> {
                log.info("Currency Name: {}", currency.getCurrencyName());
                log.info("Currency Abbreviation: {}", currency.getCurrencyAbb());

                UserWallet userWallet = UserWallet.builder()
                        .walletAddress("wallet address")
                        .currencyName(currency.getCurrencyName())
                        .currencyAbbr(currency.getCurrencyAbb())
                        .blockchainNetwork(currency.getBlockchainNetwork())
                        .userId(userId)
                        .userEmail(email)
                        .currency(currency)
                        .build();

                userWalletRepository.save(userWallet);
            });
        }
        catch (Exception e) {
            // Log the exception
            log.error(String.valueOf(e));
            e.printStackTrace();
            throw new RuntimeException("Failed to create user wallet.", e);
        }
    }

    @Override
    public List<UserWalletDto> getWallet(String userName) {

        List<UserWallet> userWalletList=this.userWalletRepository.findByUserEmail(userName);
        if (!userWalletList.isEmpty()) {
            System.out.println("userWalletList is not empty...");
            for (UserWallet userWallet : userWalletList) {
                System.out.println("currency :" + userWallet.getCurrencyName());
                System.out.println("currency Abb:" + userWallet.getCurrencyAbbr());
                System.out.println("currency blockchain:" + userWallet.getBlockchainNetwork());

            }
            List<UserWalletDto> userWalletToDtoList=this.userWalletToDtoList(userWalletList);
            return userWalletToDtoList;
        }
        System.out.println("userWalletList is empty...");
        return null;
    }

    public UserWalletDto walletToDto(UserWallet wallet)
    {
        UserWalletDto walletDto=this.modelMapper.map(wallet, UserWalletDto.class);
        return walletDto;
    }

    public UserWallet dtoToWallet(UserWalletDto walletDto)
    {
        UserWallet wallet=this.modelMapper.map(walletDto, UserWallet.class);
        return wallet;
    }

    public List<UserWalletDto> userWalletToDtoList(List<UserWallet> userWalletList) {

        System.out.println("inside userWalletToDtoList");
        List<UserWalletDto> userWalletDtos = new ArrayList<>();

        for (UserWallet userWallet : userWalletList) {
            System.out.println("currency :"+userWallet.getCurrencyName());
            System.out.println("currency Abb:"+userWallet.getCurrencyAbbr());
            System.out.println("currency blockchain:"+userWallet.getBlockchainNetwork());

            UserWalletDto userWalletDto = walletToDto(userWallet);
            userWalletDtos.add(userWalletDto);
        }
        return userWalletDtos;
    }
}
