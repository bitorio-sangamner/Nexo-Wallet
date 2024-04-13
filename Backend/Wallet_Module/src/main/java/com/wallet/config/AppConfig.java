package com.wallet.config;


import com.alibaba.fastjson.JSONObject;
import com.okex.open.api.config.APIConfiguration;
import com.okex.open.api.service.marketData.MarketDataAPIService;
import com.okex.open.api.service.marketData.impl.MarketDataAPIServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();

    }
}


