package com.nexo_wallet.repositories;

import com.nexo_wallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    @Query("SELECT t FROM Transaction t INNER JOIN t.user_own_cryptocurrency uoc INNER JOIN uoc.wallet w INNER JOIN w.user u WHERE u.email = :email")
    List<Transaction> findByUserEmail(String email);

    @Query("SELECT t FROM Transaction t INNER JOIN t.user_own_cryptocurrency uoc INNER JOIN uoc.wallet w INNER JOIN w.user u WHERE u.email = :userName AND t.currencyName = :currencyName")
    List<Transaction> findByUserEmailAndCurrencyName(String userName, String currencyName);

    Transaction findByTransactionId(Long transactionId);
}
