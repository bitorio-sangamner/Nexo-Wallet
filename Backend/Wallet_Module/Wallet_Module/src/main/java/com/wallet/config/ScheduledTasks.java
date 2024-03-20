package com.wallet.config;

import com.wallet.service.UserWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    private UserWalletService userWalletService;

    @Scheduled(fixedRate = 60000) // Update prices every minute
    public void updateCoinPricesTask() {
        userWalletService.updateCoinPrices();
    }
}
