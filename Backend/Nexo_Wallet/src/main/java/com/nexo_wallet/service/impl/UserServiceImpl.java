package com.nexo_wallet.service.impl;

import com.nexo_wallet.entity.User;
import com.nexo_wallet.entity.UserOwnCurrencies;
import com.nexo_wallet.entity.Wallet;
import com.nexo_wallet.payloads.UserDto;
import com.nexo_wallet.repositories.UserOwnCurrenciesRepository;
import com.nexo_wallet.repositories.UserRepository;
import com.nexo_wallet.repositories.WalletRepository;
import com.nexo_wallet.service.UserOwnCurrenciesService;
import com.nexo_wallet.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserOwnCurrenciesService userOwnCurrenciesService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserOwnCurrenciesRepository userOwnCurrenciesRepository;
    @Override
    public void createUser(UserDto userDto) {

        User user=this.dtoToUser(userDto);


        userRepository.save(user);
        Wallet wallet= Wallet.builder()
                .user(user)
                .build();
        walletRepository.save(wallet);
        List<UserOwnCurrencies> userOwnCurrenciesList=userOwnCurrenciesService.createUserOwnCurrencies(wallet);
        userOwnCurrenciesRepository.saveAll(userOwnCurrenciesList);

    }

    public User dtoToUser(UserDto userDto)
    {
        User user=this.modelMapper.map(userDto, User.class);
        return user;
    }

    public UserDto userToDto(User user)
    {
        UserDto userDto=this.modelMapper.map(user, UserDto.class);
        return userDto;
    }
}
