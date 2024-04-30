package com.nexo_wallet.service.impl;

import com.nexo_wallet.entity.Transaction;
import com.nexo_wallet.payloads.TransactionDto;
import com.nexo_wallet.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DownloadableStatementsService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionServiceImpl transactionServiceImpl;

    public List<Map<String, String>> generateStatement(String userName, String currencyName, String format) {
        // Retrieve transaction history for the specific coin and user from the database
        List<Transaction> transactions = transactionRepository.findByUserEmailAndCurrencyName(userName, currencyName);

        List<TransactionDto> transactionDtoList = transactionServiceImpl.transactionsToDtoList(transactions);

        List<Map<String, String>> statementList = new ArrayList<>();

        // Populate the list with transaction data
        for (TransactionDto transaction : transactionDtoList) {
            Map<String, String> transactionMap = new LinkedHashMap<>();
            transactionMap.put("Date", transaction.getTransactionDateTime().toString());
            transactionMap.put("Type", transaction.getTransactionType().toString());
            transactionMap.put("Amount", transaction.getAmount().toString());
            // Add other transaction attributes as needed (Price, Total, etc.)
            statementList.add(transactionMap);
        }

        return statementList;
    }

}
