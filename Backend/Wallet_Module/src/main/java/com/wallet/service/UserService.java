package com.wallet.service;

import com.wallet.entites.User;
import com.wallet.payloads.UserDto;
import org.springframework.stereotype.Component;

@Component
public interface UserService {

    public void saveUser(UserDto userDto);
}
