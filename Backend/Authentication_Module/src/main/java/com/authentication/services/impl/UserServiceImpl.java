package com.authentication.services.impl;

import com.authentication.entities.User;
import com.authentication.payloads.UserDto;
import com.authentication.repositories.UserRepository;
import com.authentication.services.UserService;
import com.authentication.util.EmailUtil;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.authentication.exceptions.*;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailUtil emailUtil;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Override
    public UserDto registerUser(UserDto userDto)  {

        User user=this.dtoToUser(userDto);

        User userResult=userRepository.findByEmail(user.getEmail());

        if(userResult!=null)
        {
            //throw new Exception("User with this email already exists");
            throw new ResourceAlreadyExistsException("User with",user.getEmail(),"already exists");
        }
        User savedUser=this.userRepository.save(user);

        return this.userToDto(savedUser);
    }

    @Override
    public String setPin(String email, String pin) {
        try {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                user.setPin(pin);
                userRepository.save(user);
                return "Pin set successfully!";
            } else {
                return "User not found with email: " + email;
            }
        } catch (Exception e) {
            // Log the exception for debugging purposes
            logger.error("Error occurred while setting pin for email: " + email, e);
            e.printStackTrace();
            return "Failed to set pin. Please try again later.";
        }
    }

    @Override
    public String login(String email, String password) {
        try
        {
            User user=userRepository.findByEmail(email);

            if(user!=null)
            {
               if(user.getPassword().equals(password))
               {
                   return "user found";
               }
               return "invalid password";
            }
            else {
                  return "user not found please check your credentials or sign up";
            }
        }
        catch(Exception e)
        {
           e.printStackTrace();
           return "something went wrong please try again!!";
        }

    }

    @Override
    public String forgotPassword(String email)  {

        User user=userRepository.findByEmail(email);
        if(user!=null)
        {
            try {
                emailUtil.sendSetPasswordEmail(email);
            }
            catch(MessagingException exception)
            {
               throw new RuntimeException("Unable to send set password email please try again");
            }
        }
        else{
            return "user not found with this email :"+email;
        }
        return "please check your email to set new password to your account";
    }

    @Override
    public String setPassword(String email, String newPassword) {
        try {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                user.setPassword(newPassword);
                userRepository.save(user);
                return "New password set successfully login with new password";
            }
            else {
                return "User not found with email: " + email;
            }
        }
        catch (Exception e) {

            // Log the exception for debugging purposes
            logger.error("Error occurred while setting the new password for user : " + email, e);
            // You may also handle specific types of exceptions separately
            e.printStackTrace();
            return "Failed to set new password. Please try again later.";
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
