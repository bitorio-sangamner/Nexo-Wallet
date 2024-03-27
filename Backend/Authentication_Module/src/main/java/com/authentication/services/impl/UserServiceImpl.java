package com.authentication.services.impl;

import com.authentication.entities.User;
import com.authentication.entities.UserCoinsDto;
import com.authentication.payloads.UserDto;
import com.authentication.repositories.UserRepository;
import com.authentication.services.UserWalletClient;
import com.authentication.services.UserService;
import com.authentication.util.EmailUtil;
import com.authentication.util.OtpUtil;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.authentication.exceptions.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private UserWalletClient userWalletClient;


    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Override
    public String registerUser(UserDto userDto) {
        // Convert DTO to entity
        User user = dtoToUser(userDto);

        // Check if user already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ResourceAlreadyExistsException("User with email :", user.getEmail() ," already exists");
        }

        // Generate OTP and send email
        String otp = otpUtil.generateOtp();
        logger.info("Generated OTP for {}: {}", user.getEmail(), otp);
        try {
            emailUtil.sendOtpEmail(user.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email. Please try again later.");
        }

        // Set OTP and OTP generated time, then save user
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        return "User registration successful. Check your email to verify your account.";
    }

    @Override
    public String verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email);
        if (Objects.nonNull(user)) {
            if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() <= (1 * 60))
            {
                user.setActive(true);
                userRepository.save(user);

                userWalletClient.createUserWallet((long) user.getId(),user.getEmail());

                return "Account verified. You can now login.";
            }
            else {
                return "Invalid OTP or OTP expired. Please regenerate OTP and try again.";
            }
        }
        else {
            throw new ResourceNotFoundException("User", "email", email);
        }
    }

    @Override
    public String regenerateOtp(String email) {
        return null;
    }

    @Override
    public String setPin(String email, String pin) {
        return null;
    }



//    @Override
//    public String setPin(String email, String pin) {
//        try {
//            User user = userRepository.findByEmail(email);
//            if (user != null) {
//                user.setPin(pin);
//                userRepository.save(user);
//                return "Pin set successfully!";
//            } else {
//                return "User not found with email: " + email;
//            }
//        } catch (Exception e) {
//            // Log the exception for debugging purposes
//            logger.error("Error occurred while setting pin for email: " + email, e);
//            e.printStackTrace();
//            return "Failed to set pin. Please try again later.";
//        }
//    }


    @Override
    public String login(String email, String password) {

        User user = userRepository.findByEmail(email);
        if (Objects.nonNull(user))
            {
                if (user.getPassword().equals(password)) {
                    return "user found";
                }
                else {
                    return "invalid password";
                }
            }
            else {
                throw new ResourceNotFoundException("User", "email", email);
            }
    }
    @Override
    public String forgotPassword(String email)  {

        User user=userRepository.findByEmail(email);
        if(Objects.nonNull(user))
        {
            try {
                emailUtil.sendSetPasswordEmail(email);
                return "please check your email to set a new password for your account";
            }
            catch(MessagingException exception)
            {
               throw new RuntimeException("Failed to send set password email. Please try again later.");
            }
        }
        else{
            throw new ResourceNotFoundException("User", "email", email);
        }

    }
    @Override
    public String setPassword(String email, String newPassword) {

            User user = userRepository.findByEmail(email);
            if (Objects.nonNull(user)) {
                try {
                    user.setPassword(newPassword);
                    userRepository.save(user);
                    return "New password set successfully login with new password";
                }
                catch(Exception e)
                {
                    throw new RuntimeException("Failed to set new password. Please try again later.");
                }
            }
            else {
                throw new ResourceNotFoundException("User", "email", email);
            }

    }

    @Override
    public UserDto getUserByEmail(String email) {
        return null;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return null;
    }

    @Override
    public void deleteUser(String email) {

    }

    @Override
    public List<UserCoinsDto> getAllCurrenciesHeldByUser(String email) {

        User user=userRepository.findByEmail(email);
        List<UserCoinsDto> allCurrenciesHeldByUser= userWalletClient.getCurrencyHeldByUser((long) user.getId());
        return allCurrenciesHeldByUser;
    }

    public  User dtoToUser(UserDto userDto)
    {
        User user=this.modelMapper.map(userDto,User.class);
//        user.setId(userDto.getId());
//        user.setFullname(userDto.getFullname());
//        user.setEmail(userDto.getEmail());
//        user.setPassword(userDto.getPassword());
//        user.setPin(userDto.getPin());

        return user;
    }

    public UserDto userToDto(User user)
    {
        UserDto userDto=this.modelMapper.map(user, UserDto.class);

//        userDto.setId(user.getId());
//        userDto.setEmail(user.getEmail());
//        userDto.setPassword(user.getPassword());
//        userDto.setFullname(user.getFullname());

        return userDto;

    }
}
