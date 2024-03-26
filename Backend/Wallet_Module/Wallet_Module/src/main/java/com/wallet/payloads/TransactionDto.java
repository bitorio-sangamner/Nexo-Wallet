package com.wallet.payloads;

import com.wallet.enums.TransactionStatus;
import com.wallet.enums.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@NoArgsConstructor
@Getter
@Setter
public class TransactionDto {

    private long userId;
    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;
    private String currencyName;
    private BigDecimal amount;
    private BigDecimal fiatValue;
    private BigDecimal fees;
    @Enumerated(value = EnumType.STRING)
    private TransactionStatus transactionStatus;
    private String counterparty;
}
