package com.wallet.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubUserApiKeyResponse {

    public int retCode;
    public String retMsg;
    public Result result;
    public RetExtInfo retExtInfo;
    public long time;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result{
        public String id;
        public String note;
        public String apiKey;
        public int readOnly;
        public String secret;
        public Permissions permissions;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Permissions{
        @JsonProperty("ContractTrade")
        public ArrayList<Object> contractTrade;
        @JsonProperty("Spot")
        public ArrayList<Object> spot;
        @JsonProperty("Wallet")
        public ArrayList<String> wallet;
        @JsonProperty("Options")
        public ArrayList<Object> options;
        @JsonProperty("CopyTrading")
        public ArrayList<Object> copyTrading;
        @JsonProperty("BlockTrade")
        public ArrayList<Object> blockTrade;
        @JsonProperty("Exchange")
        public ArrayList<Object> exchange;
        @JsonProperty("NFT")
        public ArrayList<Object> nFT;
        @JsonProperty("Derivatives")
        public ArrayList<Object> derivatives;
        @JsonProperty("Affiliate")
        public ArrayList<Object> affiliate;
    }

}
