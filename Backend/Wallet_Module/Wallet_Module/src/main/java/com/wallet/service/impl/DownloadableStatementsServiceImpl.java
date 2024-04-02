package com.wallet.service.impl;

//import com.opencsv.CSVWriter;
import com.wallet.entites.Transaction;
import com.wallet.payloads.TransactionDto;
import com.wallet.repositories.TransactionRepository;
import com.wallet.service.DownloadableStatementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import java.io.IOException;
//import java.io.StringWriter;
//import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class DownloadableStatementsServiceImpl implements DownloadableStatementsService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired TransactionServiceImpl transactionServiceImpl;
//    @Override
//    public byte[] generateStatement(Long userId, String currencyName, String format) {
//        // Retrieve transaction history for the specific coin and user from the database
//        List<Transaction> transactions = transactionRepository.findByUserIdAndCurrencyName(userId, currencyName);
//
//        List<TransactionDto> transactionDtoList=transactionServiceImpl.transactionsToDtoList(transactions);
//
//        // Generate CSV or PDF file based on the transactions
//        // Example: Using a CSV library like OpenCSV
//        StringWriter writer = new StringWriter();
//        try {
//            CSVWriter csvWriter = new CSVWriter(writer);
//            // Write headers
//            csvWriter.writeNext(new String[]{"Date", "Type", "Amount", "Price", "Total"});
//            // Write transaction data
//            for (TransactionDto transaction : transactionDtoList) {
//                csvWriter.writeNext(new String[]{transaction.getTransactionDateTime().toString(), String.valueOf(transaction.getTransactionType()),
//                        transaction.getAmount().toString()
//                        });
//            }//for
//            csvWriter.flush();
//        } catch (IOException e) {
//            // Handle exception
//        }
//
//        return writer.toString().getBytes(StandardCharsets.UTF_8);
//
//    }

//    @Override
//    public Map<String, List<String>> generateStatement(Long userId, String currencyName, String format) {
//        // Retrieve transaction history for the specific coin and user from the database
//        List<Transaction> transactions = transactionRepository.findByUserIdAndCurrencyName(userId, currencyName);
//
//        List<TransactionDto> transactionDtoList = transactionServiceImpl.transactionsToDtoList(transactions);
//
//        Map<String, List<String>> csvDataMap = new LinkedHashMap<>();
//
//        // Add headers to the map
//        List<String> headers = Arrays.asList("Date", "Type", "Amount", "Price", "Total");
//        csvDataMap.put("headers", headers);
//
//        // Add transaction data to the map
//        List<String> transactionData = new ArrayList<>();
//        for (TransactionDto transaction : transactionDtoList) {
//            String rowData = String.join(",", Arrays.asList(
//                    transaction.getTransactionDateTime().toString(),
//                    String.valueOf(transaction.getTransactionType()),
//                    transaction.getAmount().toString()
//            ));
//            transactionData.add(rowData);
//        }
//        csvDataMap.put("data", transactionData);
//
//        return csvDataMap;
//    }

    @Override
    public List<Map<String, String>> generateStatement(Long userId, String currencyName, String format) {
        // Retrieve transaction history for the specific coin and user from the database
        List<Transaction> transactions = transactionRepository.findByUserIdAndCurrencyName(userId, currencyName);

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
