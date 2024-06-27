package com.wallet.service.impl;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                Map<String, Object> coinInfo= (Map<String, Object>) this.bybitApiAssetRestClient.getAssetCoinInfo(assetDataRequest);
                System.out.println("coinInfo :"+coinInfo);

                Map<String, Object> result = (Map<String, Object>) coinInfo.get("result");
                List<Map<String, Object>> rows = (List<Map<String, Object>>) result.get("rows");

                if (rows != null && !rows.isEmpty()) {
                    Map<String, Object> firstRow = rows.get(0);
                    List<Map<String, Object>> chainsList = (List<Map<String, Object>>) firstRow.get("chains");
                    String byBitCoin= (String) firstRow.get("coin");

                    System.out.println("byBitCoin :"+byBitCoin);

                    if (chainsList != null && !chainsList.isEmpty()) {
                        Map<String, Object> chainInfo = chainsList.get(0);
                        String chainType = (String) chainInfo.get("chain");
                        System.out.println("chain type :" + chainType);

                        AssetDataRequest assetDataRequest1=AssetDataRequest.builder().coin(byBitCoin).subMemberId(subMemberId).chainType(chainType).build();
                        Map<String, Object> depositAddressResponse= (Map<String, Object>) this.bybitApiAssetRestClient.getAssetSubMemberDepositAddress(assetDataRequest1);

                        System.out.println("depositAddressResponse :"+depositAddressResponse);
                        Map<String, Object> depositAddressResult = (Map<String, Object>) depositAddressResponse.get("result");
                        if (depositAddressResult != null) {
                            Map<String, Object> chains = (Map<String, Object>)depositAddressResult.get("chains");
                            if (chains != null) {
                                String depositAddress=(String) chains.get("addressDeposit");
                                UserWallet userWallet = UserWallet.builder()
                                        .walletAddress(depositAddress)
                                        .currencyName(currency.getCurrencyName())
                                        .currencyAbbr(currency.getCurrencyAbb())
                                        .blockchainNetwork(currency.getBlockchainNetwork())
                                        .userId(userId)
                                        .userEmail(email)
                                        .currency(currency)
                                        .build();

                                userWalletRepository.save(userWallet);
                            }
                        }
                    } else {
                        System.out.println("No chains available");
                    }
                } else {
                    System.out.println("No rows available");
                }
            });
            return "Wallet created successfully!!";
        }
        catch (DataAccessException e) {
            log.error("DataAccessException: {}", e.getMessage());
            throw new RuntimeException("Failed to create user wallet", e);
        }

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
