package com.wallet.service;

import com.wallet.payloads.TransactionDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TransactionService {

    String saveTransaction(TransactionDto transactionDto);

    List<TransactionDto> getAllTransactions();
    List<TransactionDto> filterTransactions(/* Add filter parameters here */);
    TransactionDto searchTransactionById(String transactionId);
}
