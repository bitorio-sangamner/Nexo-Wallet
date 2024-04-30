package com.wallet.service.impl;

import com.wallet.entities.Currency;
import com.wallet.payloads.CurrencyDTO;
import com.wallet.repositories.CurrencyRepository;
import com.wallet.service.CurrencyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CurrencyDTO saveCurrency(CurrencyDTO currencyDto) {
        Currency currency=this.dtoToCurrency(currencyDto);
        return this.currencyToDto(currencyRepository.save(currency));
    }



    public Currency dtoToCurrency(CurrencyDTO currencyDTO)
    {
        Currency currency=this.modelMapper.map(currencyDTO, Currency.class);
        return currency;
    }

    public CurrencyDTO currencyToDto(Currency currency)
    {
        CurrencyDTO currencyDTO=this.modelMapper.map(currency, CurrencyDTO.class);
        return currencyDTO;
    }
}
