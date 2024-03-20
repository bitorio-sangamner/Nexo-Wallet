package com.wallet.service.impl;

import com.wallet.entites.UserWallet;
import com.wallet.enums.Currency;
import com.wallet.payloads.UserWalletDto;
import com.wallet.repositories.UserWalletRepository;
import com.wallet.service.UserWalletService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserWalletServiceImpl implements UserWalletService {

    @Autowired
    private UserWalletRepository userWalletRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<UserWalletDto> getAllCoinsOfUser() {

        List<UserWallet> allCoins= userWalletRepository.findAll();
//        return allCoins.stream()
//                .map(this::coinsToDto) // Convert each Coins entity to CoinsDto
//                .collect(Collectors.toList()); // Collect the converted objects into a list

        List<UserWalletDto> allCoinsDto = new ArrayList<>();
        for (UserWallet coin : allCoins) {
            allCoinsDto.add(coinsToDto(coin));
        }

        return allCoinsDto;
    }

    @Override
    public void updateCoinPrices() {

        // Fetch latest market prices for all coins from an external API
        // Update currentPrice field for each coin in the database
    }

    @Override
    public void createUserCoins(Long userId,String userName) {

        // Accessing enum constants using a loop
        for (Currency currency : Currency.values()) {

            System.out.println(currency.getName() + ": " + currency.getSymbol());
            UserWallet userWallet =new UserWallet();
            userWallet.setUserId(userId);
            userWallet.setUserName(userName);
            userWallet.setCurrencyName(currency.getName());
            userWallet.setSymbol(currency.getSymbol());
            userWallet.setAvailableBalance(BigDecimal.ZERO);
            userWallet.setLockedBalance(BigDecimal.ZERO);
            userWallet.setCurrentPrice(BigDecimal.ZERO);
            userWalletRepository.save(userWallet);

        }


//        for (Currency coin : Currency.values()) {
//            System.out.println("Coin: " + coin);
//
//            UserWallet userWallet =new UserWallet();
//            userWallet.setUserId(userId);
//            userWallet.setUserName(userName);
//            userWallet.setName(String.valueOf(coin));
//            userWalletRepository.save(userWallet);
//        }

    }

    @Override
    public List<UserWalletDto> getCurrencyHeldByUser(Long userId) {

        List<UserWallet> allCoinsDetailsOfUser= userWalletRepository.findAllByUserId(userId);

        List<UserWalletDto> allCoinsDetailsDto = new ArrayList<>();
        for (UserWallet coin : allCoinsDetailsOfUser) {
            allCoinsDetailsDto.add(coinsToDto(coin));
        }
        return allCoinsDetailsDto;
    }

    @Override
    public UserWalletDto searchCoin(String userName, String currency) {

        UserWallet userWallet=userWalletRepository.findByUserNameAndCurrencyName(userName,currency);
        UserWalletDto userWalletDto=this.coinsToDto(userWallet);
        return userWalletDto;
    }


    public UserWallet dtoToCoins(UserWalletDto coinsDto)
    {
        UserWallet coins=this.modelMapper.map(coinsDto, UserWallet.class);
        return coins;
    }

    public UserWalletDto coinsToDto(UserWallet coins)
    {
        UserWalletDto coinsDto=this.modelMapper.map(coins, UserWalletDto.class);
        return coinsDto;

    }
}
