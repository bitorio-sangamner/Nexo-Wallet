package com.nexo_wallet.service;

import com.nexo_wallet.payloads.TransactionDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
public interface TransactionService {

    String saveTransaction(String username, TransactionDto transactionDto);

    List<TransactionDto> filterTransactions(String userName, String cryptocurrency, Date startDate, Date endDate, Date transactionDate, String type, BigDecimal fiatValue);
    TransactionDto searchTransactionById(Long transactionId);

}

