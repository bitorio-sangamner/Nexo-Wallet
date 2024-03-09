package com.authentication.services;

import com.authentication.payloads.UserDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {

    UserDto registerUser(UserDto user) throws Exception;
    String setPin(String email,String pin);

    String login(String email,String password);
    UserDto getUserByEmail(String email);
    List<UserDto> getAllUsers();

    void deleteUser(String email);
}
