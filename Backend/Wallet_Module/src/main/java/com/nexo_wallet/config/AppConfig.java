package com.nexo_wallet.config;

import com.alibaba.fastjson.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

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
}

