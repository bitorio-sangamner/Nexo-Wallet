package com.nexo_wallet.repositories;

import com.nexo_wallet.entity.UserOwnCurrencies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserOwnCurrenciesRepository extends JpaRepository<UserOwnCurrencies,Integer> {

    @Query("SELECT uc FROM UserOwnCurrencies uc JOIN uc.wallet w JOIN w.user u WHERE u.email = :email")
    List<UserOwnCurrencies> findByUserEmail(@Param("email") String email);

    @Query("SELECT u FROM UserOwnCurrencies u WHERE u.wallet.user.email = :userEmail AND u.currencyName = :currencyName")
    UserOwnCurrencies findByUserEmailAndCurrencyName(@Param("userEmail") String userEmail, @Param("currencyName") String currencyName);
}
