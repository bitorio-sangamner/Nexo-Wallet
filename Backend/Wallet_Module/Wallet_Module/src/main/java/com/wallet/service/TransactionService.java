package com.wallet.service;

import com.wallet.payloads.TransactionDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
public interface TransactionService {

    String saveTransaction(TransactionDto transactionDto);

    List<TransactionDto> getAllTransactions();

    List<TransactionDto> filterTransactions(Long userId, String cryptocurrency, Date startDate, Date endDate, Date transactionDate,String type, BigDecimal fiatValue);

    TransactionDto searchTransactionById(Long transactionId);
}
