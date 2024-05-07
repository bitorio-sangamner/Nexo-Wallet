package com.wallet.service.impl;

import com.wallet.entities.Currency;
import com.wallet.exceptions.CurrencySaveException;
import com.wallet.payloads.CurrencyDTO;
import com.wallet.repositories.CurrencyRepository;
import com.wallet.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CurrencyDTO saveCurrency(CurrencyDTO currencyDto) {
        try {
            Currency currency = this.dtoToCurrency(currencyDto);
            Currency savedCurrency = currencyRepository.save(currency);
            return this.currencyToDto(savedCurrency);
        } catch (DataAccessException ex) {
            // Log the error
            log.error("Error occurred while saving currency: {}", ex.getMessage());
            // Rethrow as a custom exception
            throw new CurrencySaveException("Failed to save currency: " + ex.getMessage());
        }
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
