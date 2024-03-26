package com.wallet.service.impl;

import com.wallet.entites.Transaction;
import com.wallet.entites.UserWallet;
import com.wallet.enums.TransactionStatus;
import com.wallet.enums.TransactionType;
import com.wallet.payloads.TransactionDto;
import com.wallet.repositories.TransactionRepository;
import com.wallet.repositories.UserWalletRepository;
import com.wallet.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserWalletRepository userWalletRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Override
    public String saveTransaction(TransactionDto transactionDto) {

        UserWallet userWallet=userWalletRepository.findByUserIdAndCurrencyName(transactionDto.getUserId(),transactionDto.getCurrencyName());
        logger.info("user id :"+userWallet.getUserId());
        logger.info("userName :"+userWallet.getUserName());
        logger.info("currency :"+userWallet.getCurrencyName());

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


        Transaction transaction = this.dtoToTransaction(transactionDto);
         if(this.isValidTransactionType(transaction.getTransactionType())) {
             transactionRepository.save(transaction);
             return "transaction saved";
         }
         return "invalid transaction type";
    }



    @Override
    public List<TransactionDto> getAllTransactions() {
        return null;
    }

    @Override
    public List<TransactionDto> filterTransactions(long userId, String cryptocurrency, Date startDate, Date endDate, String type, BigDecimal fiatValue) {

        List<Transaction> filteredTransactions = new ArrayList<>();
        // Retrieve all transactions from the database
        List<Transaction> allTransactions = transactionRepository.findByUserId(userId);


        // Iterate through each transaction and apply filters
        for (Transaction transaction : allTransactions) {
            logger.info("userId :"+transaction.getUserId());
            logger.info("currency :"+transaction.getCurrencyName());
            logger.info("type :"+transaction.getTransactionType());

            if (!transaction.getCurrencyName().equalsIgnoreCase(cryptocurrency)) {
                continue; // Skip if cryptocurrency doesn't match
            }
//            if (startDate != null && transaction.getDate().before(startDate)) {
//                continue; // Skip if transaction date is before the start date
//            }
//            if (endDate != null && transaction.getDate().after(endDate)) {
//                continue; // Skip if transaction date is after the end date
//            }
            if (!transaction.getTransactionType().toString().equalsIgnoreCase(type)) {
                System.out.println("transaction type from database:"+transaction.getTransactionType());
                System.out.println("transaction type from user:"+type);
                continue; // Skip if transaction type doesn't match
            }
            if (fiatValue != null && transaction.getFiatValue().compareTo(fiatValue) != 0) {
                continue; // Skip if fiat value doesn't match
            }
            // If transaction passes all filters, add it to the filtered list
            filteredTransactions.add(transaction);
        }
        System.out.println("*****************************************************");
        for(Transaction transaction:filteredTransactions)
        {
            logger.info("userId :"+transaction.getUserId());
            logger.info("currency :"+transaction.getCurrencyName());
        }

        // Convert filtered transactions to TransactionDto objects
        List<TransactionDto> transactionDtoList = transactionsToDtoList(filteredTransactions);
        return transactionDtoList;
    }


    @Override
    public TransactionDto searchTransactionById(String transactionId) {
        return null;
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
