package com.wallet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bybit.api.client.domain.asset.request.AssetDataRequest;
import com.bybit.api.client.restApi.BybitApiAssetRestClient;
import com.wallet.entities.Currency;
import com.wallet.entities.UserWallet;
import com.wallet.exceptions.ResourceNotFoundException;
import com.wallet.payloads.UserWalletDto;
import com.wallet.repositories.CurrencyRepository;
import com.wallet.repositories.UserWalletRepository;
import com.wallet.service.UserWalletService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
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

    @Autowired
    private BybitApiAssetRestClient bybitApiAssetRestClient;
    @Override
    public void createWallet(Long userId, String email) {
        try {
            List<Currency> currencies = currencyRepository.findAll();
            if (currencies.isEmpty()) {
                throw new ResourceNotFoundException("Currencies not found");
            }
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
        } catch (DataAccessException e) {
            log.error("DataAccessException: {}", e.getMessage());
            throw new RuntimeException("Failed to create user wallet", e);
        }
    }

    @Override
    public String createWallet(Long userId, String email,String subMemberId) {

        try {
            List<Currency> currencies = currencyRepository.findAll();
            if (currencies.isEmpty()) {
                throw new ResourceNotFoundException("Currencies not found");
            }
            currencies.forEach(currency -> {
                log.info("Currency Name: {}", currency.getCurrencyName());
                log.info("Currency Abbreviation: {}", currency.getCurrencyAbb());
                String coin=currency.getCurrencyAbb();

                AssetDataRequest assetDataRequest= AssetDataRequest.builder().coin(coin).build();
                Object coinInfo=this.bybitApiAssetRestClient.getAssetCoinInfo(assetDataRequest);
                //JSONObject coinInfo1= (JSONObject) this.bybitApiAssetRestClient.getAssetCoinInfo(assetDataRequest);

                // Print the class name to understand what type of object is returned
                System.out.println("Returned object class: " + coinInfo.getClass().getName());

                // Use reflection to inspect the fields (optional, for debugging purposes)
                for (Field field : coinInfo.getClass().getDeclaredFields()) {
                    field.setAccessible(true);  // To access private fields
                    try {
                        System.out.println(field.getName() + " - " + field.get(coinInfo));
                        if(field.getName().equals("chainType"))
                        {
                            AssetDataRequest assetDataRequest2= AssetDataRequest.builder().coin(coin).chainType(String.valueOf(field.get(coinInfo))).subMemberId(subMemberId).build();
                             Object depositAddress=this.bybitApiAssetRestClient.getAssetSubMemberDepositAddress(assetDataRequest2);

                            // Print the class name to understand what type of object is returned
                            System.out.println("Returned object class: " + depositAddress.getClass().getName());

                            // Use reflection to inspect the fields (optional, for debugging purposes)
                            for (Field field2 : depositAddress.getClass().getDeclaredFields()) {
                                field.setAccessible(true);  // To access private fields
                                try {
                                    System.out.println(field.getName() + " - " + field.get(depositAddress));

                                    if(field.getName().equals("field.getName()"))
                                    {
                                        UserWallet userWallet = UserWallet.builder()
                                                .walletAddress((String) field.get(depositAddress))
                                                .currencyName(currency.getCurrencyName())
                                                .currencyAbbr(currency.getCurrencyAbb())
                                                .blockchainNetwork(currency.getBlockchainNetwork())
                                                .userId(userId)
                                                .userEmail(email)
                                                .currency(currency)
                                                .build();

                                        userWalletRepository.save(userWallet);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }


            });
        } catch (DataAccessException e) {
            log.error("DataAccessException: {}", e.getMessage());
            throw new RuntimeException("Failed to create user wallet", e);
        }
        return null;
    }

    @Override
    public List<UserWalletDto> getWallet(String userName) {
        List<UserWallet> userWalletList = this.userWalletRepository.findByUserEmail(userName);
        if (!userWalletList.isEmpty()) {
            log.info("User wallet list is not empty for user: {}", userName);
            List<UserWalletDto> userWalletToDtoList = this.userWalletToDtoList(userWalletList);
            return userWalletToDtoList;
        } else {
            log.info("User wallet list is empty for user: {}", userName);
            throw new ResourceNotFoundException("User wallet not found for user: " + userName);
        }
    }
    public UserWalletDto walletToDto(UserWallet wallet) {
        UserWalletDto walletDto=this.modelMapper.map(wallet, UserWalletDto.class);
        return walletDto;
    }

    public UserWallet dtoToWallet(UserWalletDto walletDto) {
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
