package com.wallet.repositories;

import com.wallet.entities.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserWalletRepository extends JpaRepository<UserWallet,Long> {

    //custom methods
    List<UserWallet> findByUserEmail(String email);
    @Query("SELECT uw FROM UserWallet uw WHERE uw.userEmail = :email AND uw.currency.currencyAbb = :currencyAbb")
    List<UserWallet> findByUserEmailAndCurrencyAbb(@Param("email") String email, @Param("currencyAbb") String currencyAbb);
}
