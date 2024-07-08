package com.wallet.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.wallet.util.SubUserResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface SubUserService {

    public SubUserResponse createSubUserOnBybit(String userName, String password);
}
