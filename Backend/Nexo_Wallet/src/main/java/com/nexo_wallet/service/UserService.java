package com.nexo_wallet.service;

import com.nexo_wallet.payloads.UserDto;
import org.springframework.stereotype.Component;

@Component
public interface UserService {

    void createUser(UserDto userDto);
}
