package com.wallet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.okex.open.api.service.marketData.MarketDataAPIService;
import com.wallet.service.MarketDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketDataServiceImpl implements MarketDataService {

    @Autowired
    private MarketDataAPIService marketDataAPIService;
    @Autowired
    private JSONObject jsonObject;

    private static final Logger logger= LoggerFactory.getLogger(MarketDataServiceImpl.class);
    @Override
    public JSONObject getMarketData(StringBuilder coinSymbol) {
        // Create a new StringBuilder instead of modifying the original one
        StringBuilder modifiedCoinSymbol = new StringBuilder(coinSymbol);
        modifiedCoinSymbol.append("-USD-SWAP");

        logger.info("Instrument Id: " + modifiedCoinSymbol);

        // Call the getBlockTicker method with the modified coinSymbol
        return marketDataAPIService.getBlockTicker(modifiedCoinSymbol.toString());
    }

}
