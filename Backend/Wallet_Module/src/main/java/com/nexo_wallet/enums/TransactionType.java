package com.nexo_wallet.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
    public enum TransactionType {

        @JsonProperty("deposit")
        DEPOSIT,
        @JsonProperty("withdrawal")
        WITHDRAWAL,
        @JsonProperty("trade")
        TRADE,
        @JsonProperty("staking")
        STAKING,
        @JsonProperty("lending")
        LENDING,
    }


