package com.wallet.payloads;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class SubUserDto {

    private String userId;
    private String username;
    private int memberType;
    private int status;
    private String remark;
}
