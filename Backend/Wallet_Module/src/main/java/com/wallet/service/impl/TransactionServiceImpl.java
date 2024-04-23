package com.wallet.service.impl;

import com.wallet.entites.Transaction;
import com.wallet.entites.User;
import com.wallet.entites.CurrencyWallet;
import com.wallet.enums.TransactionStatus;
import com.wallet.enums.TransactionType;
import com.wallet.exceptions.ResourceNotFoundException;
import com.wallet.payloads.TransactionDto;
import com.wallet.repositories.TransactionRepository;
import com.wallet.repositories.UserRepository;
import com.wallet.repositories.CurrencyWalletRepository;
import com.wallet.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CurrencyWalletRepository currencyWalletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Override
    public String saveTransaction(String username, TransactionDto transactionDto) {

        CurrencyWallet currencyWallet = currencyWalletRepository.findByUserEmailAndCurrencyName(username, transactionDto.getCurrencyName());

        /*
              main code to save the user transaction
              need to change
         */

//        Transaction transaction = this.dtoToTransaction(transactionDto);
//
//        if(this.isValidTransactionType(transaction.getTransactionType())) {
//            if(transaction.getTransactionType().equals("DEPOSIT"))
//            {
//                if(transactionDto.getAmount().compareTo(userWallet.getAvailableBalance()) < 0) {
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

            if(currencyWallet !=null) {
                Transaction transaction = this.dtoToTransaction(transactionDto);
                if (this.isValidTransactionType(transaction.getTransactionType())) {

                    Optional<User> optionalUser = this.userRepository.findByEmail(username);
                    if (optionalUser.isPresent()) {
                        User user = optionalUser.get();
                        transaction.setUser(user); // Set the customer for the product
                        transactionRepository.save(transaction);
                        return "transaction saved";
                    }

                }
                return "invalid transaction type";
            }
             throw new ResourceNotFoundException("UserWallet", "userName and currency", username + "," + transactionDto.getCurrencyName());


        }//saveTransaction






//    @Override
//    public List<TransactionDto> getAllTransactions() {
//        return null;
//    }
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
                    logger.info("userId :" + transaction.getUser().getEmail());
                    logger.info("currency :" + transaction.getCurrencyName());
                    logger.info("type :" + transaction.getTransactionType());

                    if (!transaction.getCurrencyName().equalsIgnoreCase(cryptocurrency)) {
                        logger.info("currency does not match...");
                        continue; // Skip if cryptocurrency doesn't match
                    }

                    if (transactionDate != null) {
                    LocalDateTime transactionLocalDateTime = transactionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();//Convert Date to LocalDateTime

                    if (transaction.getTransactionDateTime().isBefore(transactionLocalDateTime) && transaction.getTransactionDateTime().isAfter(transactionLocalDateTime)) {
                        logger.info("something is wrong in the transaction date");
                        continue;
                    }
                }

                    if (!transaction.getTransactionType().toString().equalsIgnoreCase(type)) {
                        System.out.println("transaction type from database:" + transaction.getTransactionType());
                        System.out.println("transaction type from user:" + type);
                        logger.info("Transaction type does not match...");
                        continue; // Skip if transaction type doesn't match
                    }

                    if (fiatValue != null && transaction.getFiatValue().compareTo(fiatValue) != 0) {
                        logger.info("fiatValue does not match...");
                        continue; // Skip if fiat value doesn't match
                    }

                    // If transaction passes all filters, add it to the filtered list
                    filteredTransactions.add(transaction);
                } catch (Exception e) {
                    // Log the exception or handle it appropriately
                    logger.error("Error processing transaction", e);
                }
            }//for loop

            // Convert filtered transactions to TransactionDto objects
            List<TransactionDto> transactionDtoList = transactionsToDtoList(filteredTransactions);
            return transactionDtoList;
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            logger.error("Error filtering transactions", e);
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
            logger.error("Error searching transaction by ID: " + transactionId, e);
            // Rethrow the exception or return an appropriate response based on your application's logic
            throw e;
        }
    }





    public Transaction dtoToTransaction(TransactionDto transactionDto)
    {
        Transaction transaction=this.modelMapper.map(transactionDto,Transaction.class);
        return transaction;
    }

    public TransactionDto transactionToDto(Transaction transaction)
    {
        TransactionDto transactionDto=this.modelMapper.map(transaction,TransactionDto.class);
        return transactionDto;
    }

    public List<TransactionDto> transactionsToDtoList(List<Transaction> transactions) {
        List<TransactionDto> transactionDtos = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionDto transactionDto = transactionToDto(transaction);
            transactionDtos.add(transactionDto);
        }
        return transactionDtos;
    }
    private boolean isValidTransactionType(TransactionType transactionType) {
        for (TransactionType validType : TransactionType.values()) {
            if (validType.equals(transactionType)) {
                return true;
            }
        }
        return false;
    }


    private boolean isValidTransactionStatus(TransactionStatus transactionStatus)
    {
        for(TransactionStatus validTransactionStatus : TransactionStatus.values())
        {
            if(validTransactionStatus.equals(transactionStatus))
            {
                return true;
            }
        }
        return false;
    }
}
