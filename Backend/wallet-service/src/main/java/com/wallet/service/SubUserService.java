package com.wallet.service;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface SubUserService {

    public Map<String, Object> createSubUserOnBybit(String userName, String password);
}
