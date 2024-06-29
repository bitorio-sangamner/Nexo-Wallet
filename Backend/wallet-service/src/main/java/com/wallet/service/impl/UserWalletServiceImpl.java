package com.wallet.service.impl;

import com.bybit.api.client.domain.asset.request.AssetDataRequest;
import com.bybit.api.client.restApi.BybitApiAssetRestClient;
import com.wallet.entities.Currency;
import com.wallet.entities.UserWallet;
import com.wallet.exceptions.ResourceNotFoundException;
import com.wallet.payloads.UserWalletDto;
import com.wallet.repositories.CurrencyRepository;
import com.wallet.repositories.UserWalletRepository;
import com.wallet.service.UserWalletBalanceService;
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

    @Autowired
    private UserWalletBalanceService userWalletBalanceService;

//    @Override
//    public String createWallet(Long userId, String email,String subMemberId) {
//
//        try {
//            List<Currency> currencies = currencyRepository.findAll();
//            if (currencies.isEmpty()) {
//                throw new ResourceNotFoundException("Currencies not found");
//            }
//            currencies.forEach(currency -> {
//                log.info("Currency Name: {}", currency.getCurrencyName());
//                log.info("Currency Abbreviation: {}", currency.getCurrencyAbb());
//                String coin=currency.getCurrencyAbb();
//
//                AssetDataRequest assetDataRequest= AssetDataRequest.builder().coin(coin).build();
//                Map<String, Object> coinInfo= (Map<String, Object>) this.bybitApiAssetRestClient.getAssetCoinInfo(assetDataRequest);
//                System.out.println("coinInfo :"+coinInfo);
//
//                Map<String, Object> result = (Map<String, Object>) coinInfo.get("result");
//                List<Map<String, Object>> rows = (List<Map<String, Object>>) result.get("rows");
//
//                if (rows != null && !rows.isEmpty()) {
//                    Map<String, Object> firstRow = rows.get(0);
//                    List<Map<String, Object>> chainsList = (List<Map<String, Object>>) firstRow.get("chains");
//                    String byBitCoin= (String) firstRow.get("coin");
//
//                    System.out.println("byBitCoin :"+byBitCoin);
//
//                    if (chainsList != null && !chainsList.isEmpty()) {
//                        Map<String, Object> chainInfo = chainsList.get(0);
//                        String chainType = (String) chainInfo.get("chain");
//                        System.out.println("chain type :" + chainType);
//
//                        AssetDataRequest assetDataRequest1=AssetDataRequest.builder().coin(byBitCoin).subMemberId(subMemberId).chainType(chainType).build();
//                        Map<String, Object> depositAddressResponse= (Map<String, Object>) this.bybitApiAssetRestClient.getAssetSubMemberDepositAddress(assetDataRequest1);
//
//                        System.out.println("depositAddressResponse :"+depositAddressResponse);
//                        Map<String, Object> depositAddressResult = (Map<String, Object>) depositAddressResponse.get("result");
//                        if (depositAddressResult != null) {
//                            Map<String, Object> chains = (Map<String, Object>)depositAddressResult.get("chains");
//                            if (chains != null) {
//                                String depositAddress=(String) chains.get("addressDeposit");
//                                UserWallet userWallet = UserWallet.builder()
//                                        .walletAddress(depositAddress)
//                                        .currencyName(currency.getCurrencyName())
//                                        .currencyAbbr(currency.getCurrencyAbb())
//                                        .blockchainNetwork(currency.getBlockchainNetwork())
//                                        .userId(userId)
//                                        .userEmail(email)
//                                        .currency(currency)
//                                        .build();
//
//                                userWalletRepository.save(userWallet);
//                                this.userWalletBalanceService.createUserWalletBalance(userId,email,currency);
//                            }
//                        }
//                    } else {
//                        System.out.println("No chains available");
//                    }
//                } else {
//                    System.out.println("No rows available");
//                }
//            });
//            return "Wallet created successfully!!";
//        }
//        catch (DataAccessException e) {
//            log.error("DataAccessException: {}", e.getMessage());
//            throw new RuntimeException("Failed to create user wallet", e);
//        }
//
//    }

    /**
     * Creates a wallet for the user with the given ID, email
     *
     * @param userId      The ID of the user.
     * @param email       The email of the user.
     * @param subMemberId The sub-member ID to get the deposit address from BYBIT
     * @return A success message if the wallet is created successfully.
     */
    @Override
    public String createWallet(Long userId, String email, String subMemberId) {
        try {
            // Fetch all currencies from the repository
            List<Currency> currencies = currencyRepository.findAll();
            if (currencies.isEmpty()) {
                throw new ResourceNotFoundException("Currencies not found");
            }

            // Process each currency
            for (Currency currency : currencies) {
                log.info("Currency Name: {}", currency.getCurrencyName());
                log.info("Currency Abbreviation: {}", currency.getCurrencyAbb());
                String coin = currency.getCurrencyAbb();

                // Create request to get coin info from Bybit API
                AssetDataRequest assetDataRequest = AssetDataRequest.builder().coin(coin).build();
                Map<String, Object> coinInfo = (Map<String, Object>) bybitApiAssetRestClient.getAssetCoinInfo(assetDataRequest);
                log.debug("coinInfo: {}", coinInfo);

                // Extract result from coin info response
                Map<String, Object> result = (Map<String, Object>) coinInfo.get("result");
                if (result != null) {
                    // Process each row in the result
                    List<Map<String, Object>> rows = (List<Map<String, Object>>) result.get("rows");
                    if (rows != null && !rows.isEmpty()) {
                        processCurrencyRows(rows, subMemberId, userId, email, currency);
                    } else {
                        log.warn("No rows available for currency: {}", coin);
                    }
                } else {
                    log.warn("No result available for currency: {}", coin);
                }
            }
            return "Wallet created successfully!!";
        } catch (DataAccessException e) {
            log.error("DataAccessException: {}", e.getMessage());
            throw new RuntimeException("Failed to create user wallet", e);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred while creating user wallet", e);
        }
    }

    /**
     * Processes the rows of currency information.
     *
     * @param rows        The rows of currency information.
     * @param subMemberId The sub-member ID to get deposit address from BYBIT
     * @param userId      The ID of the user.
     * @param email       The email of the user.
     * @param currency    The currency object.
     */
    private void processCurrencyRows(List<Map<String, Object>> rows, String subMemberId, Long userId, String email, Currency currency) {
        Map<String, Object> firstRow = rows.get(0);
        List<Map<String, Object>> chainsList = (List<Map<String, Object>>) firstRow.get("chains");
        String byBitCoin = (String) firstRow.get("coin");

        log.debug("byBitCoin: {}", byBitCoin);

        if (chainsList != null && !chainsList.isEmpty()) {
            processChainInfo(chainsList.get(0), byBitCoin, subMemberId, userId, email, currency);
        } else {
            log.warn("No chains available for byBitCoin: {}", byBitCoin);
        }
    }

    /**
     * Processes the chain information for a given currency.
     *
     * @param chainInfo   The chain information.
     * @param byBitCoin   The Bybit coin abbreviation.
     * @param subMemberId The sub-member ID to get deposit address from BYBIT
     * @param userId      The ID of the user.
     * @param email       The email of the user.
     * @param currency    The currency object.
     */
    private void processChainInfo(Map<String, Object> chainInfo, String byBitCoin, String subMemberId, Long userId, String email, Currency currency) {
        String chainType = (String) chainInfo.get("chain");
        log.debug("Chain type: {}", chainType);

        // Create request to get deposit address from Bybit API
        AssetDataRequest assetDataRequest = AssetDataRequest.builder()
                .coin(byBitCoin)
                .subMemberId(subMemberId)
                .chainType(chainType)
                .build();

        Map<String, Object> depositAddressResponse = (Map<String, Object>) bybitApiAssetRestClient.getAssetSubMemberDepositAddress(assetDataRequest);
        log.debug("depositAddressResponse: {}", depositAddressResponse);

        // Extract result from deposit address response
        Map<String, Object> depositAddressResult = (Map<String, Object>) depositAddressResponse.get("result");
        if (depositAddressResult != null) {
            Map<String, Object> chains = (Map<String, Object>) depositAddressResult.get("chains");
            if (chains != null) {
                String depositAddress = (String) chains.get("addressDeposit");
                createUserWallet(depositAddress, userId, email, currency);
            } else {
                log.warn("No chains found in depositAddressResult for byBitCoin: {}", byBitCoin);
            }
        } else {
            log.warn("No result found in depositAddressResponse for byBitCoin: {}", byBitCoin);
        }
    }

    /**
     * Creates a user wallet and saves it to the repository.
     *
     * @param depositAddress The deposit address for the wallet.
     * @param userId         The ID of the user.
     * @param email          The email of the user.
     * @param currency       The currency object.
     */
    private void createUserWallet(String depositAddress, Long userId, String email, Currency currency) {
        UserWallet userWallet = UserWallet.builder()
                .walletAddress(depositAddress)
                .currencyName(currency.getCurrencyName())
                .currencyAbbr(currency.getCurrencyAbb())
                .blockchainNetwork(currency.getBlockchainNetwork())
                .userId(userId)
                .userEmail(email)
                .currency(currency)
                .build();

        // Save the user wallet to the repository
        userWalletRepository.save(userWallet);

        // Create user wallet balance
        userWalletBalanceService.createUserWalletBalance(userId, email, currency);
        log.info("User wallet created for userId: {}, currency: {}", userId, currency.getCurrencyName());
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
