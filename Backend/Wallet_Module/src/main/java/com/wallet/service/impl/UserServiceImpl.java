package com.wallet.service.impl;

import com.wallet.entites.User;
import com.wallet.entites.CurrencyWallet;
import com.wallet.enums.Currency;
import com.wallet.payloads.UserDto;
import com.wallet.repositories.UserRepository;
import com.wallet.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;
    @Override
    public void saveUser(UserDto userDto) {

        List<CurrencyWallet> currencyWalletList =new ArrayList<>();


        User user=this.dtoToUser(userDto);
        for (Currency currency : Currency.values()) {
            System.out.println(currency.getName() + ": " + currency.getSymbol());

            CurrencyWallet currencyWallet =new CurrencyWallet(currency.getName() , currency.getSymbol() , BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,user);
            currencyWalletList.add(currencyWallet);
        }

        user.setCurrencyWallets(currencyWalletList);
        userRepository.save(user);


    }




    public User dtoToUser(UserDto userDto)
    {
        User user=this.modelMapper.map(userDto, User.class);
        return user;
    }
}
