package com.authentication.services;

import com.authentication.payloads.UserDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {

    UserDto createUser(UserDto user);
    UserDto getUserByEmail(String email);
    List<UserDto> getAllUsers();

    void deleteUser(String email);
}
