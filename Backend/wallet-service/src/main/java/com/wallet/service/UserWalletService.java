package com.wallet.service;

import com.wallet.payloads.UserWalletDto;
import com.wallet.payloads.UserWalletResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface UserWalletService {

    //void createWallet(Long userId,String email);
    String createWallet(Long userId, String email,String subMemberId);
    Map<String, List<UserWalletResponseDto>> getWallet(String userName);

    Map<String,UserWalletResponseDto> getWallet(String userName, String currencyAbb);
}
