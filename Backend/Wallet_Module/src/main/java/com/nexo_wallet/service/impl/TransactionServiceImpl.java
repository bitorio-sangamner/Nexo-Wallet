package com.nexo_wallet.service.impl;

import com.nexo_wallet.entity.Transaction;
import com.nexo_wallet.entity.User;
import com.nexo_wallet.entity.UserOwnCurrencies;
import com.nexo_wallet.enums.TransactionType;
import com.nexo_wallet.exceptions.ResourceNotFoundException;
import com.nexo_wallet.payloads.TransactionDto;
import com.nexo_wallet.payloads.UserDto;
import com.nexo_wallet.repositories.TransactionRepository;
import com.nexo_wallet.repositories.UserOwnCurrenciesRepository;
import com.nexo_wallet.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

   @Autowired
   private ModelMapper modelMapper;

   @Autowired
   private UserOwnCurrenciesRepository userOwnCurrenciesRepository;
   @Autowired
   private TransactionRepository transactionRepository;
    @Override
    public String saveTransaction(String username, TransactionDto transactionDto) {
        UserOwnCurrencies userOwnCurrencies=userOwnCurrenciesRepository.findByUserEmailAndCurrencyName(username,transactionDto.getCurrencyName());

      /*
              main code to save the user transaction
              need to change
         */

//        Transaction transaction = this.dtoToTransaction(transactionDto);
//
//        if(this.isValidTransactionType(transaction.getTransactionType())) {
//            if(transaction.getTransactionType().equals("DEPOSIT"))
//            {
//                if(transactionDto.getAmount().compareTo(userOwnCurrencies.getAvailableBalance()) < 0) {
//                    transactionRepository.save(transaction);
//
//                }
//                else {
//                     return "insufficient amount in your wallet";
//                }
//                return "transaction saved";
//            }
//            else if(transaction.getTransactionType().equals("TRADE")||transaction.getTransactionType().equals("STAKING")||transaction.getTransactionType().equals("LENDING"))
//             {
//                 //To-Do
//             }
//            transactionRepository.save(transaction);
//            return "transaction saved";
//
//        }
//        return "something went wrong";

        if(userOwnCurrencies !=null) {
            Transaction transaction = this.dtoToTransaction(transactionDto);
            if (this.isValidTransactionType(transaction.getTransactionType())) {
                transaction.setUser_own_cryptocurrency(userOwnCurrencies);
                transactionRepository.save(transaction);
                return "transaction saved";
            }
            return "invalid transaction type";
        }
        throw new ResourceNotFoundException("UserWallet", "userName and currency", username + "," + transactionDto.getCurrencyName());
    }

    @Override
    public List<TransactionDto> filterTransactions(String userName, String cryptocurrency, Date startDate, Date endDate, Date transactionDate, String type, BigDecimal fiatValue) {
        try {
            List<Transaction> filteredTransactions = new ArrayList<>();
            // Retrieve all transactions from the database
            List<Transaction> allTransactions = transactionRepository.findByUserEmail(userName);
            if (allTransactions.isEmpty()) {
                throw new ResourceNotFoundException("Transaction", "username", userName);
            }

            // Iterate through each transaction and apply filters
            for (Transaction transaction : allTransactions) {
                try {
                    log.info("userId :" + transaction.getUser_own_cryptocurrency().getWallet().getUser().getEmail());
                    log.info("currency :" + transaction.getCurrencyName());
                    log.info("type :" + transaction.getTransactionType());

                    if (!transaction.getCurrencyName().equalsIgnoreCase(cryptocurrency)) {
                        log.info("currency does not match...");
                        continue; // Skip if cryptocurrency doesn't match
                    }

                    if (transactionDate != null) {
                        LocalDateTime transactionLocalDateTime = transactionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();//Convert Date to LocalDateTime

                        if (transaction.getTransactionDateTime().isBefore(transactionLocalDateTime) && transaction.getTransactionDateTime().isAfter(transactionLocalDateTime)) {
                            log.info("something is wrong in the transaction date");
                            continue;
                        }
                    }

                    if (!transaction.getTransactionType().toString().equalsIgnoreCase(type)) {
                        System.out.println("transaction type from database:" + transaction.getTransactionType());
                        System.out.println("transaction type from user:" + type);
                        log.info("Transaction type does not match...");
                        continue; // Skip if transaction type doesn't match
                    }

                    if (fiatValue != null && transaction.getFiatValue().compareTo(fiatValue) != 0) {
                        log.info("fiatValue does not match...");
                        continue; // Skip if fiat value doesn't match
                    }

                    // If transaction passes all filters, add it to the filtered list
                    filteredTransactions.add(transaction);
                } catch (Exception e) {
                    // Log the exception or handle it appropriately
                    log.error("Error processing transaction", e);
                }
            }//for loop

            // Convert filtered transactions to TransactionDto objects
            List<TransactionDto> transactionDtoList = transactionsToDtoList(filteredTransactions);
            return transactionDtoList;
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            log.error("Error filtering transactions", e);
            // Rethrow the exception or return an appropriate response based on your application's logic
            throw e;
        }
    }

    @Override
    public TransactionDto searchTransactionById(Long transactionId) {
        try {
            Transaction transaction = transactionRepository.findByTransactionId(transactionId);
            if (transaction != null) {
                return this.transactionToDto(transaction);
            } else {
                throw new ResourceNotFoundException("Transaction", "transaction id", transactionId.toString());
            }
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            log.error("Error searching transaction by ID: " + transactionId, e);
            // Rethrow the exception or return an appropriate response based on your application's logic
            throw e;
        }
    }


    public Transaction dtoToTransaction(TransactionDto transactionDto)
    {
        Transaction transaction=this.modelMapper.map(transactionDto, Transaction.class);
        return transaction;
    }

    public TransactionDto transactionToDto(Transaction transaction)
    {
        TransactionDto transactionDto=this.modelMapper.map(transaction, TransactionDto.class);
        return transactionDto;
    }


    private boolean isValidTransactionType(TransactionType transactionType) {
        for (TransactionType validType : TransactionType.values()) {
            if (validType.equals(transactionType)) {
                return true;
            }
        }
        return false;
    }

    public List<TransactionDto> transactionsToDtoList(List<Transaction> transactions) {
        List<TransactionDto> transactionDtos = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionDto transactionDto = transactionToDto(transaction);
            transactionDtos.add(transactionDto);
        }
        return transactionDtos;
    }
}
