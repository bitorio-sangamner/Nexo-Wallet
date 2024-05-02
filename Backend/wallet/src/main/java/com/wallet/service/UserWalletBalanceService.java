package com.wallet.service;

import org.springframework.stereotype.Component;

@Component
public interface UserWalletBalanceService {

     void createUserWalletBalance(Long id,String email);
}
