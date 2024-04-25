package com.nexo_wallet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserOwnCurrencies {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int userCurrencyId;

    private String currencyName;
    private String symbol;
    private BigDecimal marketPrice;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private BigDecimal spotBalance = BigDecimal.ZERO;
    private BigDecimal lockBalance = BigDecimal.ZERO;
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    @JsonIgnore // Add this annotation to break circular reference
    private Wallet wallet;

    @OneToMany(mappedBy = "user_own_cryptocurrency", targetEntity = Transaction.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactionList;


}
