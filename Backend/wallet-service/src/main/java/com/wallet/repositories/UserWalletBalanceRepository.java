package com.wallet.repositories;

import com.wallet.entities.UserWalletBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserWalletBalanceRepository extends JpaRepository<UserWalletBalance,Long> {

    //custom method
     UserWalletBalance findByEmailAndCurrencyName(String email,String currencyName);
     List<UserWalletBalance> findByEmail(String email);
}
