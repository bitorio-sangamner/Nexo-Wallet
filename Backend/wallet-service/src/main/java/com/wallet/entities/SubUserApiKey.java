package com.wallet.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SubUserApiKey {

    @Id
    private int id;
    private String note;
    private String apiKey;

    private int readOnly;
    private String secret;
    private List<String> walletPermissions;
    private List<Object> spotPermissions;
    private List<Object> contractTradePermissions;
    private List<Object> optionsPermissions;
    private List<Object> copyTradingPermissions;
    private List<Object> blockTradePermissions;
    private List<Object> exchangePermissions;
    private List<Object> nFTPermissions;

}
