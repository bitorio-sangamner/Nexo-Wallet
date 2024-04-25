package com.nexo_wallet.payloads;

import com.nexo_wallet.entity.UserOwnCurrencies;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WalletDto {

    private int walletId;
    private int userId;
    private List<UserOwnCurrenciesDto> userOwnCurrenciesList;
}
