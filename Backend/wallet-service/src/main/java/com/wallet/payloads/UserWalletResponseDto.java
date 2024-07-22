package com.wallet.payloads;

import com.wallet.entities.UserWallet;
import com.wallet.entities.UserWalletBalance;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWalletResponseDto {
    private List<WalletDetailsDto> listOfUserWalletDetails;

    private WalletBalanceDetailsDto userWalletBalanceDetails;
    private Long userId;

//    private String currencyAbb;
//
//    private String blockChainNetwork;

}
