package com.wallet.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CurrencyWalletProvider {

    @JsonProperty("none")
    NONE,

    @JsonProperty("nexo")
    NEXO,

    @JsonProperty("self")
    SELF
}
