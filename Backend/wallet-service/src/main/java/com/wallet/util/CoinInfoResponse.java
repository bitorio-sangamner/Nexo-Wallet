package com.wallet.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoinInfoResponse {
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
        private ArrayList<Row> rows;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Row {
        private String name;
        private String coin;
        private String remainAmount;
        private ArrayList<Chain> chains;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Chain {
        private String chainType;
        private String confirmation;
        private String withdrawFee;
        private String depositMin;
        private String withdrawMin;
        private String chain;
        private String chainDeposit;
        private String chainWithdraw;
        private String minAccuracy;
        private String withdrawPercentageFee;
    }
}
