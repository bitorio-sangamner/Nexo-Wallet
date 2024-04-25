package com.nexo_wallet.payloads;

import com.nexo_wallet.enums.TransactionStatus;
import com.nexo_wallet.enums.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TransactionDto {

    private String currencyName;
    private LocalDateTime transactionDateTime;
    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal fiatValue;
    private BigDecimal fees;
    @Enumerated(value = EnumType.STRING)
    private TransactionStatus transactionStatus;
    private String counterparty;
}
