package com.wallet.config;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.restApi.BybitApiAssetRestClient;
import com.bybit.api.client.restApi.BybitApiUserRestClient;
import com.bybit.api.client.service.BybitApiClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BybitConfigClass {

    private String ApiKey="vs9lqFBTGzOPXvN1jS";
    private String SecretKey="GOCldMZCzWIQmBgMadpiITULCM4tuRTmHx14";

    @Bean
    public BybitApiAssetRestClient bybitApiAssetRestClient()
    {
        BybitApiAssetRestClient client=BybitApiClientFactory.newInstance(ApiKey, SecretKey, BybitApiConfig.MAINNET_DOMAIN).newAssetRestClient();
        return client;

    }

    @Bean
    public BybitApiUserRestClient bybitApiUserRestClient()
    {
        BybitApiUserRestClient client =BybitApiClientFactory.newInstance(ApiKey, SecretKey, BybitApiConfig.MAINNET_DOMAIN).newUserRestClient();
        return client;
    }


}
