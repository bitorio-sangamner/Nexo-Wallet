package com.wallet.service;


import com.wallet.payloads.CurrencyDTO;
import org.springframework.stereotype.Component;

@Component
public interface CurrencyService {

    public CurrencyDTO saveCurrency(CurrencyDTO currencyDto);
}
