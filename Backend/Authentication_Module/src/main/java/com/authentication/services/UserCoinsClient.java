package com.authentication.services;


import com.authentication.entities.UserCoinsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@FeignClient(name = "WALLET-SERVICE", url = "http://localhost:9091") // Specify the URL of the service
public interface UserCoinsClient {

    @PostMapping("/userCoins/createUserCoinsDetails/{userId}")
    public void createUserCoinsDetails(@PathVariable Long userId);

    @GetMapping("/userCoins/getCurrencyHeldByUser/{userId}")
    public List<UserCoinsDto> getCurrencyHeldByUser(@PathVariable Long userId);
}
