package com.wallet.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubDepositeAddressResponse {
    public int retCode;
    public String retMsg;
    public Result result;
    private RetExtInfo retExtInfo;
    public long time;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        public String coin;
        public Chains chains;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Chains {
        public String chainType;
        public String addressDeposit;
        public String tagDeposit;
        public String chain;
        public String batchReleaseLimit;
    }
}
