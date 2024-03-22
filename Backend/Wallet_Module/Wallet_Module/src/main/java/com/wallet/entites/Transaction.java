package com.wallet.entites;

import com.wallet.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long transactionId;
    private long userId;
    private LocalDateTime transactionDateTime;
    private String currencyName;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal fiatValue;
    private BigDecimal fees;
    private String status;
    private String counterparty;

    @PrePersist
    //This method will be executed just before the entity is persisted.
    protected void onCreate() {
        transactionDateTime = LocalDateTime.now();
    }
}
