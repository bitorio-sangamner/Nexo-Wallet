package com.wallet.enums;

import java.util.HashMap;
import java.util.Map;

public enum Currency {

    BITCOIN("Bitcoin", "BTC"),
    ETHEREUM("Ethereum", "ETH"),
    RIPPLE("Ripple", "XRP"),
    LITECOIN("Litecoin", "LTC"),
    CARDANO("Cardano", "ADA"),
    STELLAR("Stellar", "XLM");

    private final String name;
    private final String symbol;

    private static final Map<String, String> currencySymbolMap = new HashMap<>();

    // Static initialization block to populate the map
    static {
        for (Currency currency : Currency.values()) {
            currencySymbolMap.put(currency.getName(), currency.getSymbol());
        }
    }

    Currency(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public static String getSymbolByName(String name) {
        return currencySymbolMap.get(name);
    }

}
