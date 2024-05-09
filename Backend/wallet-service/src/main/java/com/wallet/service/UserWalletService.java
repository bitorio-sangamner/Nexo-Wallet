package com.wallet.service;

import com.wallet.payloads.UserWalletDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserWalletService {

    void createWallet(Long userId,String email);
    List<UserWalletDto> getWallet(String userName);
}
