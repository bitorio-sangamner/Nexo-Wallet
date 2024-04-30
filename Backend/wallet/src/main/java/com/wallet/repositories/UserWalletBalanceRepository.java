package com.wallet.repositories;

import com.wallet.entities.UserWalletBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWalletBalanceRepository extends JpaRepository<UserWalletBalance,Long> {
}
