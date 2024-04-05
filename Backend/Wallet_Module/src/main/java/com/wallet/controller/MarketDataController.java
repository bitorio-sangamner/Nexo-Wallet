package com.wallet.controller;

import com.alibaba.fastjson.JSONObject;
import com.wallet.service.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/marketData")
public class MarketDataController {

    @Autowired
    private MarketDataService marketDataService;

    @GetMapping("/getMarketData")
    public ResponseEntity<Object> getMarketData(@RequestBody JSONObject jsonObject)
    {
        StringBuilder coinSymbol= new StringBuilder(jsonObject.getString("coinSymbol"));
        JSONObject json=marketDataService.getMarketData(coinSymbol);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
