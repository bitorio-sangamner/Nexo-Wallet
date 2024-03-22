package com.wallet.service.impl;

import com.wallet.entites.Transaction;
import com.wallet.entites.UserWallet;
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
    public List<TransactionDto> filterTransactions() {
        return null;
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

    private boolean isValidTransactionType(TransactionType transactionType) {
        for (TransactionType validType : TransactionType.values()) {
            if (validType.equals(transactionType)) {
                return true;
            }
        }
        return false;
    }
}
