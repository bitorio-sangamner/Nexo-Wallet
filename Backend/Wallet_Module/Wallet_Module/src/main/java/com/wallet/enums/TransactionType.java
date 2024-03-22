package com.wallet.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TransactionType {

    @JsonProperty("DEPOSIT")
    DEPOSIT,
    @JsonProperty("WITHDRAWAL")
    WITHDRAWAL,
    @JsonProperty("TRADE")
    TRADE,
    @JsonProperty("STAKING")
    STAKING,
    @JsonProperty("LENDING")
    LENDING,
}
