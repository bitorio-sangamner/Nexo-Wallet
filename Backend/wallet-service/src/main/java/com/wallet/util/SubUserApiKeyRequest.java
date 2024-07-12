package com.wallet.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubUserApiKeyRequest {

    public int subuid;
    public String note;
    public int readOnly;
    public Permissions permissions;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Permissions {

        public ArrayList<String> wallet;
        public ArrayList<String> spot;
        public ArrayList<String> contractTrade;
        public ArrayList<String> options;
        public ArrayList<String> exchange;
        public ArrayList<String> copyTrading;
    }
}


