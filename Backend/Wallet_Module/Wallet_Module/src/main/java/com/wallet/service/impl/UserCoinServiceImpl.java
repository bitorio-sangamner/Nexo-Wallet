package com.wallet.service.impl;

import com.wallet.entites.UserCoinsDetails;
import com.wallet.enums.UserCoins;
import com.wallet.payloads.UserCoinsDto;
import com.wallet.repositories.UserCoinsRepository;
import com.wallet.service.UserCoinService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserCoinServiceImpl implements UserCoinService {

    @Autowired
    private UserCoinsRepository userCoinsRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<UserCoinsDto> getAllCoinsOfUser() {

        List<UserCoinsDetails> allCoins= userCoinsRepository.findAll();
//        return allCoins.stream()
//                .map(this::coinsToDto) // Convert each Coins entity to CoinsDto
//                .collect(Collectors.toList()); // Collect the converted objects into a list

        List<UserCoinsDto> allCoinsDto = new ArrayList<>();
        for (UserCoinsDetails coin : allCoins) {
            allCoinsDto.add(coinsToDto(coin));
        }

        return allCoinsDto;
    }

    @Override
    public void updateCoinPrices() {

        // Fetch latest market prices for all coins from an external API
        // Update currentPrice field for each coin in the database
    }

    @Override
    public void createUserCoins(Long userId) {

        for (UserCoins coin : UserCoins.values()) {
            System.out.println("Coin: " + coin);

            UserCoinsDetails userCoinsDetails=new UserCoinsDetails();
            userCoinsDetails.setUserId(userId);
            userCoinsDetails.setName(String.valueOf(coin));
            userCoinsRepository.save(userCoinsDetails);
        }

    }

    @Override
    public List<UserCoinsDto> getCurrencyHeldByUser(Long userId) {

        List<UserCoinsDetails> allCoinsDetailsOfUser=userCoinsRepository.findAllByUserId(userId);

        List<UserCoinsDto> allCoinsDetailsDto = new ArrayList<>();
        for (UserCoinsDetails coin : allCoinsDetailsOfUser) {
            allCoinsDetailsDto.add(coinsToDto(coin));
        }
        return allCoinsDetailsDto;
    }


    public UserCoinsDetails dtoToCoins(UserCoinsDto coinsDto)
    {
        UserCoinsDetails coins=this.modelMapper.map(coinsDto, UserCoinsDetails.class);
        return coins;
    }

    public UserCoinsDto coinsToDto(UserCoinsDetails coins)
    {
        UserCoinsDto coinsDto=this.modelMapper.map(coins, UserCoinsDto.class);
        return coinsDto;

    }
}
