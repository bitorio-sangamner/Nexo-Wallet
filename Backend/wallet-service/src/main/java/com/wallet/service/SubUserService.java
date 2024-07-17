package com.wallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.wallet.payloads.SubUserDto;
import com.wallet.util.SubUserApiKeyRequest;
import com.wallet.util.SubUserResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface SubUserService {

    public SubUserResponse createSubUserOnBybit(String userName, String email,String password);
    public SubUserDto getSubUserByEmail(String email);
    public String createSubUserApiKey(SubUserApiKeyRequest subUserApiKeyRequest,String email) throws Exception;
}
