package com.nexo_wallet.repositories;

import com.nexo_wallet.entity.Wallet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet,Integer> {

    //custom methods

    Wallet findByUserEmail(String email);

//    @EntityGraph(attributePaths = "userOwnCurrenciesList")
//    Wallet findByUserEmail(String email);
}
