package com.wallet.config;

import com.alibaba.fastjson.JSONObject;
import com.okex.open.api.config.APIConfiguration;
import com.okex.open.api.service.marketData.MarketDataAPIService;
import com.okex.open.api.service.marketData.impl.MarketDataAPIServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ConfigClass {

    @Bean
    public APIConfiguration okexApiConfig() {

        APIConfiguration apiConfiguration = new APIConfiguration();

        apiConfiguration.setDomain("https://www.okx.com");

        apiConfiguration.setApiKey("7163298c-07ed-4bc4-aff9-a6973e2422d7");
        apiConfiguration.setSecretKey("9EDA33B0AC08D2E0C462F0E127A83436");
        apiConfiguration.setPassphrase("75c60758-beA19");
        apiConfiguration.setxSimulatedTrading("1");
        apiConfiguration.setPrint(true);
        return apiConfiguration;
    }

    @Bean
    public MarketDataAPIService marketDataAPIService(APIConfiguration apiConfiguration)
    {
        return new MarketDataAPIServiceImpl(apiConfiguration);
    }
    @Bean
    public ModelMapper modelMapper()
    {
        return new ModelMapper();
    }

    @Bean
    public JSONObject jsonObject()
    {
        return new JSONObject();
    }
}
