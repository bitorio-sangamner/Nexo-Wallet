package com.wallet.service.impl;

import com.bybit.api.client.domain.asset.request.AssetDataRequest;
import com.bybit.api.client.restApi.BybitApiAssetRestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.wallet.entities.Currency;
import com.wallet.entities.UserWallet;
import com.wallet.entities.UserWalletBalance;
import com.wallet.exceptions.ResourceNotFoundException;
import com.wallet.payloads.UserWalletDto;
import com.wallet.payloads.UserWalletResponseDto;
import com.wallet.payloads.WalletBalanceDetailsDto;
import com.wallet.payloads.WalletDetailsDto;
import com.wallet.repositories.CurrencyRepository;
import com.wallet.repositories.UserWalletRepository;
import com.wallet.service.UserWalletBalanceService;
import com.wallet.service.UserWalletService;
import com.wallet.util.CoinInfoResponse;
import com.wallet.util.JsonConverter;
import com.wallet.util.SubDepositeAddressResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private ObjectMapper objectMapper;

    //    /**
//     * Creates a wallet for the user with the given ID, email
//     *
//     * @param userId      The ID of the user.
//     * @param email       The email of the user.
//     * @param subMemberId The sub-member ID to get the deposit address from BYBIT
//     * @return A success message if the wallet is created successfully.
//     */
//    @Override
//    public String createWallet(Long userId, String email, String subMemberId) {
//        try {
//            // Fetch all currencies from the repository
//            List<Currency> currencies = currencyRepository.findAll();
//            if (currencies.isEmpty()) {
//                throw new ResourceNotFoundException("Currencies not found",false);
//            }
//
//            // Process each currency
//            for (Currency currency : currencies) {
//                log.info("Currency Name: {}", currency.getCurrencyName());
//                log.info("Currency Abbreviation: {}", currency.getCurrencyAbb());
//                String coin = currency.getCurrencyAbb();
//
//                // Create request to get coin info from Bybit API
//                AssetDataRequest assetDataRequest = AssetDataRequest.builder().coin(coin).build();
//                Map<String, Object> coinInfo = (Map<String, Object>) bybitApiAssetRestClient.getAssetCoinInfo(assetDataRequest);
//
//                //JSONPObject jsonpObject = (JSONPObject) bybitApiAssetRestClient.getAssetCoinInfo(assetDataRequest);
//                log.debug("coinInfo: {}", coinInfo);
//
//                // Extract result from coin info response
//                Map<String, Object> result = (Map<String, Object>) coinInfo.get("result");
//                if (result != null) {
//                    // Process each row in the result
//                    List<Map<String, Object>> rows = (List<Map<String, Object>>) result.get("rows");
//                    if (rows != null && !rows.isEmpty()) {
//                        processCurrencyRows(rows, subMemberId, userId, email, currency);
//                    } else {
//                        log.warn("No rows available for currency: {}", coin);
//                    }
//                } else {
//                    log.warn("No result available for currency: {}", coin);
//                }
//            }
//            return "Wallet created successfully!!";
//        } catch (DataAccessException e) {
//            log.error("DataAccessException: {}", e.getMessage());
//            throw new RuntimeException("Failed to create user wallet", e);
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage());
//            throw new RuntimeException("An unexpected error occurred while creating user wallet", e);
//        }
//    }
//
//    /**
//     * Processes the rows of currency information.
//     *
//     * @param rows        The rows of currency information.
//     * @param subMemberId The sub-member ID to get deposit address from BYBIT
//     * @param userId      The ID of the user.
//     * @param email       The email of the user.
//     * @param currency    The currency object.
//     */
//    private void processCurrencyRows(List<Map<String, Object>> rows, String subMemberId, Long userId, String email, Currency currency) {
//        Map<String, Object> firstRow = rows.get(0);
//        List<Map<String, Object>> chainsList = (List<Map<String, Object>>) firstRow.get("chains");
//        String byBitCoin = (String) firstRow.get("coin");
//
//        log.debug("byBitCoin: {}", byBitCoin);
//
//        if (chainsList != null && !chainsList.isEmpty()) {
//            processChainInfo(chainsList.get(0), byBitCoin, subMemberId, userId, email, currency);
//        } else {
//            log.warn("No chains available for byBitCoin: {}", byBitCoin);
//        }
//    }
//
//    /**
//     * Processes the chain information for a given currency.
//     *
//     * @param chainInfo   The chain information.
//     * @param byBitCoin   The Bybit coin abbreviation.
//     * @param subMemberId The sub-member ID to get deposit address from BYBIT
//     * @param userId      The ID of the user.
//     * @param email       The email of the user.
//     * @param currency    The currency object.
//     */
//    private void processChainInfo(Map<String, Object> chainInfo, String byBitCoin, String subMemberId, Long userId, String email, Currency currency) {
//        String chainType = (String) chainInfo.get("chain");
//        log.debug("Chain type: {}", chainType);
//
//        // Create request to get deposit address from Bybit API
//        AssetDataRequest assetDataRequest = AssetDataRequest.builder()
//                .coin(byBitCoin)
//                .subMemberId(subMemberId)
//                .chainType(chainType)
//                .build();
//
//        Map<String, Object> depositAddressResponse = (Map<String, Object>) bybitApiAssetRestClient.getAssetSubMemberDepositAddress(assetDataRequest);
//        log.debug("depositAddressResponse: {}", depositAddressResponse);
//
//        // Extract result from deposit address response
//        Map<String, Object> depositAddressResult = (Map<String, Object>) depositAddressResponse.get("result");
//        if (depositAddressResult != null) {
//            Map<String, Object> chains = (Map<String, Object>) depositAddressResult.get("chains");
//            if (chains != null) {
//                String depositAddress = (String) chains.get("addressDeposit");
//                createUserWallet(depositAddress, userId, email, currency);
//            } else {
//                log.warn("No chains found in depositAddressResult for byBitCoin: {}", byBitCoin);
//            }
//        } else {
//            log.warn("No result found in depositAddressResponse for byBitCoin: {}", byBitCoin);
//        }
//    }
//
//    /**
//     * Creates a user wallet and saves it to the repository.
//     *
//     * @param depositAddress The deposit address for the wallet.
//     * @param userId         The ID of the user.
//     * @param email          The email of the user.
//     * @param currency       The currency object.
//     */
//    private void createUserWallet(String depositAddress, Long userId, String email, Currency currency) {
//        UserWallet userWallet = UserWallet.builder()
//                .walletAddress(depositAddress)
//                //.currencyName(currency.getCurrencyName())
//                //.currencyAbbr(currency.getCurrencyAbb())
//                //.blockchainNetwork(currency.getBlockchainNetwork())
//                .userId(userId)
//                .userEmail(email)
//                .currency(currency)
//                .build();
//
//        // Save the user wallet to the repository
//        userWalletRepository.save(userWallet);
//
//        // Create user wallet balance
//        userWalletBalanceService.createUserWalletBalance(userId, email, currency);
//        log.info("User wallet created for userId: {}, currency: {}", userId, currency.getCurrencyName());
//    }
//
//


    /**
     * Creates wallets for the specified user and email.
     *
     * @param userId     The ID of the user.
     * @param email      The email of the user.
     * @param subMemberId The sub-member ID.
     * @return A message indicating the result of the operation.
     */
    @Override
    public String createWallet(Long userId, String email, String subMemberId) {
        try {
            // Fetch all currencies from the repository
            List<Currency> currencies = currencyRepository.findAll();
            if (currencies.isEmpty()) {
                throw new ResourceNotFoundException("Currencies not found", false);
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

                //// Convert LinkedHashMap to JSON string
                String coinInfoJson = objectMapper.writeValueAsString(coinInfo);
                CoinInfoResponse coinInfoResponse = JsonConverter.convertJsonToJavaUsingJackson(coinInfoJson, CoinInfoResponse.class);

                // Process each row in the result
                if (coinInfoResponse.getResult() != null && coinInfoResponse.getResult().getRows() != null) {
                    processCurrencyRows(coinInfoResponse.getResult().getRows(), subMemberId, userId, email, currency);
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
     * Processes the currency rows and handles chain information.
     *
     * @param rows       The list of currency rows.
     * @param subMemberId The sub-member ID.
     * @param userId     The ID of the user.
     * @param email      The email of the user.
     * @param currency   The currency object.
     * @throws Exception if an error occurs while processing.
     */
    private void processCurrencyRows(List<CoinInfoResponse.Row> rows, String subMemberId, Long userId, String email, Currency currency) throws Exception {
        CoinInfoResponse.Row firstRow = rows.get(0);
        List<CoinInfoResponse.Chain> chainsList = firstRow.getChains();
        String byBitCoin = firstRow.getCoin();

        log.debug("byBitCoin: {}", byBitCoin);

        if (chainsList != null && !chainsList.isEmpty()) {
            processChainInfo(chainsList.get(0), byBitCoin, subMemberId, userId, email, currency);
        } else {
            log.warn("No chains available for byBitCoin: {}", byBitCoin);
        }
    }

    /**
     * Processes the chain information.
     *
     * @param chainInfo  The chain information.
     * @param subMemberId The sub-member ID.
     * @param userId     The ID of the user.
     * @param email      The email of the user.
     * @param currency   The currency object.
     * @throws Exception if an error occurs while processing.
     */
    private void processChainInfo(CoinInfoResponse.Chain chainInfo, String byBitCoin, String subMemberId, Long userId, String email, Currency currency) throws Exception {
        String chainType = chainInfo.getChain();
        log.debug("Chain type: {}", chainType);

        // Create request to get deposit address from Bybit API
        AssetDataRequest assetDataRequest = AssetDataRequest.builder()
                .coin(byBitCoin)
                .subMemberId(subMemberId)
                .chainType(chainType)
                .build();

        Map<String, Object> depositAddressResponse = (Map<String, Object>) bybitApiAssetRestClient.getAssetSubMemberDepositAddress(assetDataRequest);
        log.debug("depositAddressResponse: {}", depositAddressResponse);

        // Convert the depositAddressResponse map to JSON string and then to SubDepositeAddressResponse
        String depositAddressJson = objectMapper.writeValueAsString(depositAddressResponse);
        SubDepositeAddressResponse depositAddressResult = JsonConverter.convertJsonToJavaUsingJackson(depositAddressJson, SubDepositeAddressResponse.class);

        if (depositAddressResult.getResult() != null && depositAddressResult.getResult().getChains() != null) {
            String depositAddress = depositAddressResult.getResult().getChains().getAddressDeposit();
            createUserWallet(depositAddress, userId, email, currency);
        } else {
            log.warn("No result found in depositAddressResponse for byBitCoin: {}", byBitCoin);
        }
    }

    /**
     * Creates a user wallet and associates it with the specified currency and deposit address.
     *
     * @param depositAddress The deposit address.
     * @param userId         The ID of the user.
     * @param email          The email of the user.
     * @param currency       The currency object.
     */
    private void createUserWallet(String depositAddress, Long userId, String email, Currency currency) {
        try {
            // Create user wallet balance with initial zero balances
            String message = userWalletBalanceService.createUserWalletBalance(userId, email, currency);

            // Build thpe UserWallet entity
            UserWallet userWallet = UserWallet.builder()
                    .walletAddress(depositAddress)
                    .userId(userId)
                    .userEmail(email)
                    .currency(currency)
                    .build();

            // Save the UserWallet entity to the repository
            userWalletRepository.save(userWallet);

            log.info("User wallet created successfully for userId: {}, currency: {}", userId, currency.getCurrencyName());
        } catch (Exception e) {
            log.error("Failed to create user wallet for userId: {}, currency: {}", userId, currency.getCurrencyName(), e);
            throw new RuntimeException("An error occurred while creating user wallet.", e);
        }
    }


//    @Override
//    public Map<String, List<UserWalletResponseDto>> getWallet(String userName) {
//
//        List<UserWallet> userWalletList = this.userWalletRepository.findByUserEmail(userName);
//
//        // Prepare response map
//        Map<String, List<UserWalletResponseDto>> responseMap = new HashMap<>();
//        if (userWalletList.isEmpty()) {
//            throw new RuntimeException("No wallet found for the given email");
//        }
//
//        userWalletList.forEach(userWallet -> {
//            // Process userWallet
//            System.out.println(userWallet.getWalletAddress());
//            List<WalletDetailsDto> walletDetailsDtoList = new ArrayList<>();
//            WalletDetailsDto walletDetailsDto = new WalletDetailsDto(
//                    userWallet.getWalletAddress(),
//                    userWallet.getCurrency().getCurrencyAbb(),
//                    userWallet.getCurrency().getBlockchainNetwork()
//            );
//            walletDetailsDtoList.add(walletDetailsDto);
//
//            UserWalletBalance userWalletBalance = userWalletBalanceService.getWalletBalance(userName, userWallet.getCurrency().getCurrencyAbb());
//            // Create wallet balance details DTO
//            WalletBalanceDetailsDto walletBalanceDetailsDto = new WalletBalanceDetailsDto(
//                    userWalletBalance.getFundingWallet(),
//                    userWalletBalance.getTradingWallet()
//            );
//            UserWalletResponseDto responseDto = UserWalletResponseDto.builder()
//                    .listOfUserWalletDetails(walletDetailsDtoList)
//                    .userId(userWallet.getUserId())
//                    .userWalletBalanceDetails(walletBalanceDetailsDto)
//                    .build();
//
//            // Add the responseDto to the list associated with the currency abbreviation
//            // Check if the responseMap already has a list for the current currency abbreviation
//            if (!responseMap.containsKey(userWallet.getCurrency().getCurrencyAbb())) {
//
//                // If not, create a new list and put it in the map
//                responseMap.put(userWallet.getCurrency().getCurrencyAbb(), new ArrayList<>());
//            }
//
////            // Get the list associated with the current currency abbreviation
////            List<UserWalletResponseDto> responseDtoList = responseMap.get(userWallet.getCurrency().getCurrencyAbb());
////           // Add the responseDto to this list
////            responseDtoList.add(responseDto);
//
//            // Now add the responseDto to the list for the current currency abbreviation
//            responseMap.get(userWallet.getCurrency().getCurrencyAbb()).add(responseDto);
//        });
//
//        return responseMap;
//    }

    @Override
    public Map<String, List<UserWalletResponseDto>> getWallet(String userName) {
        try {
            List<UserWallet> userWalletList = userWalletRepository.findByUserEmail(userName);
            if (userWalletList.isEmpty()) {
                log.info("No wallet found for email: {}", userName);
                throw new ResourceNotFoundException("No wallet found for the given email: " + userName,false);
            }

            Map<String, List<UserWalletResponseDto>> responseMap = new HashMap<>();
            for (UserWallet userWallet : userWalletList) {
                processUserWallet(userWallet, responseMap);
            }

            return responseMap;

        } catch (Exception e) {
            log.error("An error occurred while retrieving the wallet for user: {}", userName, e);
            throw new RuntimeException("An error occurred while retrieving the wallet for user: " + userName, e);
        }
    }

    private void processUserWallet(UserWallet userWallet, Map<String, List<UserWalletResponseDto>> responseMap) {
        log.info("Processing wallet for address: {}", userWallet.getWalletAddress());

        List<WalletDetailsDto> walletDetailsDtoList = new ArrayList<>();
        WalletDetailsDto walletDetailsDto = new WalletDetailsDto(
                userWallet.getWalletAddress(),
                userWallet.getCurrency().getCurrencyAbb(),
                userWallet.getCurrency().getBlockchainNetwork()
        );
        walletDetailsDtoList.add(walletDetailsDto);

        UserWalletBalance userWalletBalance = userWalletBalanceService.getWalletBalance(
                userWallet.getUserEmail(), userWallet.getCurrency().getCurrencyAbb()
        );

        WalletBalanceDetailsDto walletBalanceDetailsDto = new WalletBalanceDetailsDto(
                userWalletBalance.getFundingWallet(),
                userWalletBalance.getTradingWallet()
        );

        UserWalletResponseDto responseDto = UserWalletResponseDto.builder()
                .listOfUserWalletDetails(walletDetailsDtoList)
                .userId(userWallet.getUserId())
                .userWalletBalanceDetails(walletBalanceDetailsDto)
                .build();
        if (!responseMap.containsKey(userWallet.getCurrency().getCurrencyAbb())) {

                // If not, create a new list and put it in the map
                responseMap.put(userWallet.getCurrency().getCurrencyAbb(), new ArrayList<>());
            }
        // Now add the responseDto to the list for the current currency abbreviation
           responseMap.get(userWallet.getCurrency().getCurrencyAbb()).add(responseDto);
        responseMap.computeIfAbsent(userWallet.getCurrency().getCurrencyAbb(), k -> new ArrayList<>()).add(responseDto);
    }

//    @Override
//    public Map<String,UserWalletResponseDto> getWallet(String userName, String currencyAbb) {
//
//        // Fetch user wallets by email and currency abbreviation
//        List<UserWallet> userWallets = userWalletRepository.findByUserEmailAndCurrencyAbb(userName, currencyAbb);
//
//        if (userWallets.isEmpty()) {
//            throw new RuntimeException("No wallet found for the given email and currency abbreviation");
//        }
//
//        // Fetch user wallet balance for the given user and currency abbreviation
//        UserWalletBalance userWalletBalance = userWalletBalanceService.getWalletBalance(userName, currencyAbb);
//
//        List<WalletDetailsDto> walletDetailsDtoList = userWallets.stream()
//                .map(uw -> new WalletDetailsDto(
//                        uw.getWalletAddress(),
//                        uw.getCurrency().getCurrencyAbb(),   // Assuming getCurrencyAbb() exists in the Currency class
//                        uw.getCurrency().getBlockchainNetwork()  // Assuming getBlockchainNetwork() exists in the Currency class
//                ))
//                .collect(Collectors.toList());
//
//        // Create wallet balance details DTO
//        WalletBalanceDetailsDto walletBalanceDetailsDto = new WalletBalanceDetailsDto(
//                userWalletBalance.getFundingWallet(),
//                userWalletBalance.getTradingWallet()
//        );
//
//        // Create response DTO
//        UserWalletResponseDto responseDto = UserWalletResponseDto.builder()
//                .listOfUserWalletDetails(walletDetailsDtoList)
//                .userId(userWallets.get(0).getUserId())
//                //.currencyAbb(userWalletBalance.getCurrencyAbb())
//                //.blockChainNetwork(userWallets.get(0).getCurrency().getBlockchainNetwork())
//                .userWalletBalanceDetails(walletBalanceDetailsDto)
//                .build();
//
//        // Prepare response map
//        Map<String, UserWalletResponseDto> responseMap = new HashMap<>();
//        responseMap.put(currencyAbb, responseDto);
//
//        return responseMap;
//    }

    @Override
    public Map<String, UserWalletResponseDto> getWallet(String userName, String currencyAbb) {
        try {
            // Fetch user wallets by email and currency abbreviation
            List<UserWallet> userWallets = userWalletRepository.findByUserEmailAndCurrencyAbb(userName, currencyAbb);

            if (userWallets.isEmpty()) {
                log.info("No wallet found for email: {} and currency: {}", userName, currencyAbb);
                throw new ResourceNotFoundException("No wallet found for the given email and currency abbreviation",false);
            }

            // Fetch user wallet balance for the given user and currency abbreviation
            UserWalletBalance userWalletBalance = userWalletBalanceService.getWalletBalance(userName, currencyAbb);

            List<WalletDetailsDto> walletDetailsDtoList = userWallets.stream()
                    .map(uw -> new WalletDetailsDto(
                            uw.getWalletAddress(),
                            uw.getCurrency().getCurrencyAbb(),
                            uw.getCurrency().getBlockchainNetwork()
                    ))
                    .collect(Collectors.toList());

            // Create wallet balance details DTO
            WalletBalanceDetailsDto walletBalanceDetailsDto = new WalletBalanceDetailsDto(
                    userWalletBalance.getFundingWallet(),
                    userWalletBalance.getTradingWallet()
            );

            // Create response DTO
            UserWalletResponseDto responseDto = UserWalletResponseDto.builder()
                    .listOfUserWalletDetails(walletDetailsDtoList)
                    .userId(userWallets.get(0).getUserId())
                    .userWalletBalanceDetails(walletBalanceDetailsDto)
                    .build();

            // Prepare response map
            Map<String, UserWalletResponseDto> responseMap = new HashMap<>();
            responseMap.put(currencyAbb, responseDto);

            return responseMap;

        } catch (Exception e) {
            log.error("Error occurred while fetching wallet for user: {} and currency: {}", userName, currencyAbb, e);
            throw new RuntimeException("An error occurred while retrieving the wallet", e);
        }
    }

    public UserWalletDto walletToDto(UserWallet wallet) {
        return this.modelMapper.map(wallet, UserWalletDto.class);
    }

    public UserWallet dtoToWallet(UserWalletDto walletDto) {
        UserWallet wallet=this.modelMapper.map(walletDto, UserWallet.class);
        return wallet;
    }

    public List<UserWalletDto> userWalletToDtoList(List<UserWallet> userWalletList) {

        System.out.println("inside userWalletToDtoList");
        List<UserWalletDto> userWalletDtos = new ArrayList<>();

        for (UserWallet userWallet : userWalletList) {
            //System.out.println("currency :"+userWallet.getCurrencyName());
            //System.out.println("currency Abb:"+userWallet.getCurrencyAbbr());
            //System.out.println("currency blockchain:"+userWallet.getBlockchainNetwork());

            UserWalletDto userWalletDto = walletToDto(userWallet);
            userWalletDtos.add(userWalletDto);
        }
        return userWalletDtos;
    }
}
