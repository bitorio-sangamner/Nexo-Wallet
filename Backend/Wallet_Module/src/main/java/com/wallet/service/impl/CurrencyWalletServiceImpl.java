package com.wallet.service.impl;

import com.wallet.entites.CurrencyWallet;
import com.wallet.exceptions.ResourceNotFoundException;
import com.wallet.payloads.CurrencyWalletDto;
import com.wallet.repositories.CurrencyWalletRepository;
import com.wallet.service.CurrencyWalletService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyWalletServiceImpl implements CurrencyWalletService {

    @Autowired
    private CurrencyWalletRepository currencyWalletRepository;

    private final Logger logger= LoggerFactory.getLogger(CurrencyWalletServiceImpl.class);

    @Autowired
    private ModelMapper modelMapper;
//    @Override
//    public List<UserWalletDto> getAllCoinsOfUser() {
//
//        List<UserWallet> allCoins= userWalletRepository.findAll();
////        return allCoins.stream()
////                .map(this::coinsToDto) // Convert each Coins entity to CoinsDto
////                .collect(Collectors.toList()); // Collect the converted objects into a list
//
//        List<UserWalletDto> allCoinsDto = new ArrayList<>();
//        for (UserWallet coin : allCoins) {
//            allCoinsDto.add(coinsToDto(coin));
//        }
//
//        return allCoinsDto;
//    }

//    @Override
//    public void updateCoinPrices() {
//
//        // Fetch latest market prices for all coins from an external API
//        // Update currentPrice field for each coin in the database
//    }

//    @Override
//    public void createUserWallet(Long userId, String userName) {
//        try {
//            // Accessing enum constants using a loop
//            for (Currency currency : Currency.values()) {
//                System.out.println(currency.getName() + ": " + currency.getSymbol());
////                UserWallet userWallet = new UserWallet();
////                userWallet.setUserId(userId);
////                userWallet.setUserName(userName);
////                userWallet.setCurrencyName(currency.getName());
////                userWallet.setSymbol(currency.getSymbol());
////                userWallet.setAvailableBalance(BigDecimal.ZERO);
////                userWallet.setLockedBalance(BigDecimal.ZERO);
////                userWallet.setCurrentPrice(BigDecimal.ZERO);
//
//                UserWallet userWallet=new UserWallet(userId,userName, currency.getName() , currency.getSymbol() ,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO);
//                try {
//                    userWalletRepository.save(userWallet);
//                } catch (Exception e) {
//                    // Log the exception or handle it appropriately
//                    logger.error("Error saving user wallet for currency: " + currency.getName(), e);
//                    e.printStackTrace();
//                }
//            }
//        }
//        catch (Exception e) {
//            // Log the exception or handle it appropriately
//            logger.error("Error creating user wallet", e);
//            e.printStackTrace();
//        }
//    }


    @Override
    public List<CurrencyWalletDto> getCurrencyHeldByUser(String userName) {
        try {
            List<CurrencyWallet> allCoinsDetailsOfUser = currencyWalletRepository.findAllByUserEmail(userName);
            if (!allCoinsDetailsOfUser.isEmpty()) {
                List<CurrencyWalletDto> allCoinsDetailsDto = new ArrayList<>();
                for (CurrencyWallet coin : allCoinsDetailsOfUser) {
                    allCoinsDetailsDto.add(coinsToDto(coin));
                }
                return allCoinsDetailsDto;
            } else {
                throw new ResourceNotFoundException("UserWallet", "userName", userName);
            }
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            logger.error("Error retrieving user wallet details for username: " + userName, e);
            // Rethrow the exception or return an appropriate response based on your application's logic
            throw e;
        }
    }


    @Override
    public CurrencyWalletDto searchCoin(String userName, String currency) {
        try {
            CurrencyWallet currencyWallet = currencyWalletRepository.findByUserEmailAndCurrencyName(userName, currency);
            if (currencyWallet != null) {
                return this.coinsToDto(currencyWallet);
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



    public CurrencyWallet dtoToCoins(CurrencyWalletDto coinsDto)
    {
        CurrencyWallet coins=this.modelMapper.map(coinsDto, CurrencyWallet.class);
        return coins;
    }

    public CurrencyWalletDto coinsToDto(CurrencyWallet coins)
    {
        System.out.println("Currency name:"+coins.getCurrencyName());
        System.out.println("Currency symbol:"+coins.getSymbol());
        System.out.println("Currency symbol:"+coins.getUser().getEmail());

        CurrencyWalletDto coinsDto=this.modelMapper.map(coins, CurrencyWalletDto.class);
        return coinsDto;

    }
}
