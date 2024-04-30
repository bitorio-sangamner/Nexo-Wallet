package com.nexo_wallet.repositories;

import com.nexo_wallet.entity.PlatformCurrencies;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformCurrencyRepository extends JpaRepository<PlatformCurrencies,Integer> {
}
