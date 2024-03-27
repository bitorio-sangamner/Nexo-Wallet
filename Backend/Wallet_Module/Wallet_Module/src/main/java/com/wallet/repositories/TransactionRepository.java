package com.wallet.repositories;

import com.wallet.entites.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findByUserId(Long userId);
    Transaction findByTransactionId(Long transactionId);
}
