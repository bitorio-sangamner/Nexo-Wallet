package com.wallet.repositories;

import com.wallet.entites.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserWalletRepository extends JpaRepository<UserWallet,Integer> {

    //custom methods
    List<UserWallet> findAllByUserId(Long userId);
    UserWallet findByUserNameAndCurrencyName(String userName,String currencyName);

    UserWallet findByUserIdAndCurrencyName(Long userId,String currencyName);
}
