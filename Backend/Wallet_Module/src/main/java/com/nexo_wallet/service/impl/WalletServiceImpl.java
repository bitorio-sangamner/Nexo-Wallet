package com.nexo_wallet.service.impl;


import com.nexo_wallet.entity.Wallet;
import com.nexo_wallet.exceptions.ResourceNotFoundException;
import com.nexo_wallet.payloads.WalletDto;
import com.nexo_wallet.repositories.WalletRepository;
import com.nexo_wallet.service.WalletService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public WalletDto getWalletByEmailWithCurrencies(String userName) {
        Wallet wallet = walletRepository.findByUserEmail(userName);
        if (wallet == null) {
            throw new ResourceNotFoundException("Wallet", "user email", userName);
        }
        return walletToDto(wallet);
    }


    public WalletDto walletToDto(Wallet wallet)
    {
        WalletDto walletDto=this.modelMapper.map(wallet, WalletDto.class);
        return walletDto;
    }

    public Wallet dtoToWallet(WalletDto walletDto)
    {
        Wallet wallet=this.modelMapper.map(walletDto, Wallet.class);
        return wallet;
    }

 }

