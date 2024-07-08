package com.wallet.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubUserResponse {
    private int retCode;
    private String retMsg;
    private Result result;
    private RetExtInfo retExtInfo;
    private long time;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private String uid;
        private String username;
        private int memberType;
        private int status;
        private String remark;
    }
}
