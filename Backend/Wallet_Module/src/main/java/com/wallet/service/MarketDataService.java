package com.wallet.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface MarketDataService {

    JSONObject getMarketData(StringBuilder coinSymbol);
}
