package com.wallet.service.impl;

import com.wallet.entites.UserWallet;
import com.wallet.enums.Currency;
import com.wallet.exceptions.ResourceNotFoundException;
import com.wallet.payloads.UserWalletDto;
import com.wallet.repositories.UserWalletRepository;
import com.wallet.service.UserWalletService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserWalletServiceImpl implements UserWalletService {

    @Autowired
    private UserWalletRepository userWalletRepository;

    private final Logger logger= LoggerFactory.getLogger(UserWalletServiceImpl.class);

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
    public void createUserWallet(Long userId, String userName) {
        try {
            // Accessing enum constants using a loop
            for (Currency currency : Currency.values()) {
                System.out.println(currency.getName() + ": " + currency.getSymbol());
//                UserWallet userWallet = new UserWallet();
//                userWallet.setUserId(userId);
//                userWallet.setUserName(userName);
//                userWallet.setCurrencyName(currency.getName());
//                userWallet.setSymbol(currency.getSymbol());
//                userWallet.setAvailableBalance(BigDecimal.ZERO);
//                userWallet.setLockedBalance(BigDecimal.ZERO);
//                userWallet.setCurrentPrice(BigDecimal.ZERO);

                UserWallet userWallet=new UserWallet(userId,userName, currency.getName() , currency.getSymbol() ,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO);
                try {
                    userWalletRepository.save(userWallet);
                } catch (Exception e) {
                    // Log the exception or handle it appropriately
                    logger.error("Error saving user wallet for currency: " + currency.getName(), e);
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            // Log the exception or handle it appropriately
            logger.error("Error creating user wallet", e);
            e.printStackTrace();
        }
    }


    @Override
    public List<UserWalletDto> getCurrencyHeldByUser(Long userId) {
        try {
            List<UserWallet> allCoinsDetailsOfUser = userWalletRepository.findAllByUserId(userId);
            if (!allCoinsDetailsOfUser.isEmpty()) {
                List<UserWalletDto> allCoinsDetailsDto = new ArrayList<>();
                for (UserWallet coin : allCoinsDetailsOfUser) {
                    allCoinsDetailsDto.add(coinsToDto(coin));
                }
                return allCoinsDetailsDto;
            } else {
                throw new ResourceNotFoundException("UserWallet", "user id", userId.toString());
            }
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            logger.error("Error retrieving user wallet details for user ID: " + userId, e);
            // Rethrow the exception or return an appropriate response based on your application's logic
            throw e;
        }
    }


    @Override
    public UserWalletDto searchCoin(String userName, String currency) {
        try {
            UserWallet userWallet = userWalletRepository.findByUserNameAndCurrencyName(userName, currency);
            if (userWallet != null) {
                return this.coinsToDto(userWallet);
            } else {
                throw new ResourceNotFoundException("UserWallet", "userName and currency", userName + "," + currency);
            }
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            logger.error("Error searching user wallet for userName: " + userName + " and currency: " + currency, e);
            // Rethrow the exception or return an appropriate response based on your application's logic
            throw e;
        }
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
