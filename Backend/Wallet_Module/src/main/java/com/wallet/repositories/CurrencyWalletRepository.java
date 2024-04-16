package com.wallet.repositories;

import com.wallet.entites.CurrencyWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyWalletRepository extends JpaRepository<CurrencyWallet,Integer> {

    //custom methods
    List<CurrencyWallet> findAllByUserEmail(String email);
    CurrencyWallet findByUserEmailAndCurrencyName(String userName, String currencyName);
}
