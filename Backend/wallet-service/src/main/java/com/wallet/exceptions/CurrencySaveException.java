package com.wallet.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencySaveException extends RuntimeException{

    String message;
    public CurrencySaveException(String message)
    {
        super(message);
        this.message=message;
    }
}
