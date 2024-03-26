package com.wallet.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TransactionStatus {
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("completed")
    COMPLETED,
    @JsonProperty("failed")
    FAILED
}
