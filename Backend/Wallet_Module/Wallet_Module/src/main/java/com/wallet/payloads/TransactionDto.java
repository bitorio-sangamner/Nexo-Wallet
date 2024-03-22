package com.wallet.payloads;

import com.wallet.enums.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@NoArgsConstructor
@Getter
@Setter
public class TransactionDto {

    private long userId;
    private TransactionType transactionType;
    private String currencyName;
    private BigDecimal amount;
    private BigDecimal fiatValue;
    private BigDecimal fees;
    private String status;
    private String counterparty;
}
