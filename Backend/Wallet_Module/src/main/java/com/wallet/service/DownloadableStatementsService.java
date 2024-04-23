package com.wallet.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface DownloadableStatementsService {

//    Map<String, List<String>> generateStatement(Long userId, String currencyName, String format);

    List<Map<String, String>> generateStatement(String userName, String currencyName, String format);
}
