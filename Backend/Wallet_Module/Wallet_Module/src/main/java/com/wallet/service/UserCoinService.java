package com.wallet.service;

import com.wallet.payloads.UserCoinsDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserCoinService {

    List<UserCoinsDto> getAllCoinsOfUser();
    public void updateCoinPrices() ;

    public void createUserCoins(Long userId);
    public List<UserCoinsDto> getCurrencyHeldByUser(Long userId);


}
