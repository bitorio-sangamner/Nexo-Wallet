package com.nexo_wallet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nexo_wallet.enums.TransactionStatus;
import com.nexo_wallet.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long transactionId;
    private LocalDateTime transactionDateTime;
    private String currencyName;
    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal fiatValue;
    private BigDecimal fees;
    @Enumerated(value = EnumType.STRING)
    private TransactionStatus transactionStatus;
    private String counterparty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @JsonIgnore // Add this annotation to break circular reference
    private UserOwnCurrencies user_own_cryptocurrency;

    @PrePersist
    //This method will be executed just before the entity is persisted.
    protected void onCreate() {
        transactionDateTime = LocalDateTime.now();
    }
}
