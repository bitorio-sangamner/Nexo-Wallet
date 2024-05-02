package com.wallet.repositories;

import com.wallet.entities.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserWalletRepository extends JpaRepository<UserWallet,Long> {

    //custom methods
    List<UserWallet> findByUserEmail(String email);
}
