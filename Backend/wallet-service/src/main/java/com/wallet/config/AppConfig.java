package com.wallet.config;

import com.alibaba.fastjson.JSONObject;
import com.bybit.api.client.service.BybitApiClientFactory;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    private final String baseUrl="https://api.bybit.com";


    @Bean
    public ModelMapper modelMapper()
    {
        ModelMapper modelMapper = new ModelMapper();

        return new ModelMapper();
    }

    @Bean
    public JSONObject jsonObject()
    {
        return new JSONObject();
    }


    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }


}

