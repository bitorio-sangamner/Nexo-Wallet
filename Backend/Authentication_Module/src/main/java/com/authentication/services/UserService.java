package com.authentication.services;

import com.authentication.payloads.UserDto;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {

    String registerUser(UserDto user) throws Exception;
    String verifyAccount(String email,String otp);
    String regenerateOtp(String email);
    String setPin(String email,String pin);

    String login(String email,String password);

    String forgotPassword(String email) throws MessagingException;

    String setPassword(String email,String newPassword);
    UserDto getUserByEmail(String email);
    List<UserDto> getAllUsers();

    void deleteUser(String email);
}
