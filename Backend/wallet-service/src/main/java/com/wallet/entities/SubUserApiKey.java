package com.wallet.entities;

import jakarta.persistence.*;
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
//    private List<String> walletPermissions;
//    private List<Object> spotPermissions;
//    private List<Object> contractTradePermissions;
//    private List<Object> optionsPermissions;
//    private List<Object> copyTradingPermissions;
//    private List<Object> blockTradePermissions;
//    private List<Object> exchangePermissions;
//    private List<Object> nFTPermissions;

    @ElementCollection
    @CollectionTable(name = "wallet_permissions", joinColumns = @JoinColumn(name = "sub_user_api_key_id"))
    @Column(name = "permission")
    private List<String> walletPermissions;

    @ElementCollection
    @CollectionTable(name = "spot_permissions", joinColumns = @JoinColumn(name = "sub_user_api_key_id"))
    @Column(name = "permission")
    private List<String> spotPermissions;

    @ElementCollection
    @CollectionTable(name = "contract_trade_permissions", joinColumns = @JoinColumn(name = "sub_user_api_key_id"))
    @Column(name = "permission")
    private List<String> contractTradePermissions;

    @ElementCollection
    @CollectionTable(name = "options_permissions", joinColumns = @JoinColumn(name = "sub_user_api_key_id"))
    @Column(name = "permission")
    private List<String> optionsPermissions;

    @ElementCollection
    @CollectionTable(name = "copy_trading_permissions", joinColumns = @JoinColumn(name = "sub_user_api_key_id"))
    @Column(name = "permission")
    private List<String> copyTradingPermissions;

    @ElementCollection
    @CollectionTable(name = "block_trade_permissions", joinColumns = @JoinColumn(name = "sub_user_api_key_id"))
    @Column(name = "permission")
    private List<String> blockTradePermissions;

    @ElementCollection
    @CollectionTable(name = "exchange_permissions", joinColumns = @JoinColumn(name = "sub_user_api_key_id"))
    @Column(name = "permission")
    private List<String> exchangePermissions;

    @ElementCollection
    @CollectionTable(name = "nft_permissions", joinColumns = @JoinColumn(name = "sub_user_api_key_id"))
    @Column(name = "permission")
    private List<String> nftPermissions;

}
