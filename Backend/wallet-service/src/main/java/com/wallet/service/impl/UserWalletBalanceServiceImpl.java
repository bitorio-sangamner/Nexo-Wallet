package com.wallet.service.impl;

import com.wallet.entities.Currency;
import com.wallet.entities.UserWallet;
import com.wallet.entities.UserWalletBalance;
import com.wallet.exceptions.ResourceNotFoundException;
import com.wallet.payloads.ApiResponse;
import com.wallet.payloads.UserWalletBalanceDto;
import com.wallet.payloads.UserWalletDto;
import com.wallet.repositories.CurrencyRepository;
import com.wallet.repositories.UserWalletBalanceRepository;
import com.wallet.service.UserWalletBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
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
//    @Override
//    public void createUserWalletBalance(Long id, String email) {
//        try {
//            List<Currency> currencies = currencyRepository.findAll();
//            currencies.forEach(currency -> {
//                System.out.println("Currency Name: " + currency.getCurrencyName());
//                System.out.println("Currency Abbreviation: " + currency.getCurrencyAbb());
//
//                UserWalletBalance userWalletBalance = UserWalletBalance.builder()
//                        .userId(id)
//                        .email(email)
//                        .currencyName(currency.getCurrencyName())
//                        .currencyAbb(currency.getCurrencyAbb())
//                        .fundingWallet(BigDecimal.ZERO)
//                        .tradingWallet(BigDecimal.ZERO)
//                        .build();
//
//                userWalletBalanceRepository.save(userWalletBalance);
//            });
//        } catch (Exception e) {
//            // Log the exception
//            log.error(String.valueOf(e));
//            e.printStackTrace();
//            throw new RuntimeException("Failed to create user wallet balance.", e);
//        }
//    }



    /**
     * Creates a user wallet balance entry for a new user.
     *
     * @return The created UserWalletBalance object.
     */
    @Override
    public String  createUserWalletBalance(Long userId,String email,Currency currency) {
        log.info("Creating user wallet balance");

        try {
            UserWalletBalance userWalletBalance=userWalletBalanceRepository.findByEmailAndCurrencyAbb(email,currency.getCurrencyAbb());
            if(userWalletBalance==null) {
                // Initialize a new UserWalletBalance object with zero balances
                 userWalletBalance = UserWalletBalance.builder()
                        .userId(userId)
                        .email(email)
                        .currencyAbb(currency.getCurrencyAbb())
                        .currencyName(currency.getCurrencyName())
                        .fundingWallet(BigDecimal.ZERO)
                        .tradingWallet(BigDecimal.ZERO)
                        .build();

                // Save the UserWalletBalance object to the repository
                userWalletBalance = userWalletBalanceRepository.save(userWalletBalance);

                log.info("User wallet balance created successfully with ID: {}", userWalletBalance.getId());
                return "User wallet balance created successfully";
            }
            return "balance exist";
        } catch (Exception e) {
            log.error("Failed to create user wallet balance: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create user wallet balance.", e);
        }
    }
    @Override
    public UserWalletBalanceDto getUserCurrencyByUserEmailAndCurrencyName(String email, String currencyName) {
        return null;
    }

    @Override
    public UserWalletBalance getWalletBalance(String email,String currencyAbb) {
        UserWalletBalance userWalletBalance=userWalletBalanceRepository.findByEmailAndCurrencyAbb(email,currencyAbb);
        return userWalletBalance;
    }


//    @Override
//    public UserWalletBalanceDto getUserCurrencyByUserEmailAndCurrencyName(String email, String currencyName) {
//        UserWalletBalance userWalletBalance = this.userWalletBalanceRepository.findByEmailAndCurrencyName(email, currencyName);
//        log.info("inside getUserCurrencyByUserEmailAndCurrencyName");
//        if (userWalletBalance != null) {
//            return this.userWalletBalanceToDto(userWalletBalance);
//        } else {
//            throw new ResourceNotFoundException("User wallet balance not found for email: " + email + " and currency: " + currencyName,false);
//        }
//    }
//
//    @Override
//    public List<UserWalletBalanceDto> getWalletBalance(String email) {
//        List<UserWalletBalance> userWalletBalanceList=this.userWalletBalanceRepository.findByEmail(email);
//        if (userWalletBalanceList.isEmpty()) {
//            log.info("No wallet balances found for email: {}", email);
//            var apiResponse = new ApiResponse("No wallet balances found for email: {}" + email, "error",null);
//
//            throw new ResourceNotFoundException("User wallet not found for user: " + email,false);
//        }
//        List<UserWalletBalanceDto> userWalletBalanceDtoList = new ArrayList<>();
//        for (UserWalletBalance walletBalance : userWalletBalanceList) {
//            UserWalletBalanceDto walletBalanceDto = userWalletBalanceToDto(walletBalance);
//            userWalletBalanceDtoList.add(walletBalanceDto);
//        }
//        return userWalletBalanceDtoList;
//    }

    /**
     * Converts a UserWalletBalance entity to a UserWalletBalanceDto.
     *
     * @param walletBalance The UserWalletBalance entity.
     * @return The corresponding UserWalletBalanceDto.
     */
    public UserWalletBalanceDto userWalletBalanceToDto(UserWalletBalance walletBalance) {
        UserWalletBalanceDto walletBalanceDto=this.modelMapper.map(walletBalance, UserWalletBalanceDto.class);
        return walletBalanceDto;
    }

    /**
     * Converts a UserWalletBalanceDto to a UserWalletBalance entity.
     *
     * @param walletBalanceDtoDto The UserWalletBalanceDto.
     * @return The corresponding UserWalletBalance entity.
     */
    public UserWalletBalance dtoToUserWalletBalance(UserWalletBalanceDto walletBalanceDtoDto) {
        UserWalletBalance walletBalance=this.modelMapper.map(walletBalanceDtoDto, UserWalletBalance.class);
        return walletBalance;
    }
}
