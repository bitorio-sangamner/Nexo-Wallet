package com.wallet.exceptions;

public class UserOwnCurrenciesNotFoundException extends RuntimeException{

    public UserOwnCurrenciesNotFoundException(String message) {
        super(message);
    }
}
