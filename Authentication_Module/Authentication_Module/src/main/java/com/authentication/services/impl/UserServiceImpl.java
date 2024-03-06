package com.authentication.services.impl;

import com.authentication.entities.User;
import com.authentication.payloads.UserDto;
import com.authentication.repositories.UserRepository;
import com.authentication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDto createUser(UserDto userDto) {

        User user=this.dtoToUser(userDto);

        User savedUser=this.userRepository.save(user);

        return this.userToDto(savedUser);
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
        User user=new User();
        user.setId(userDto.getId());
        user.setFullname(userDto.getFullname());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPin(userDto.getPin());

        return user;
    }

    public UserDto userToDto(User user)
    {
        UserDto userDto=new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setFullname(user.getFullname());

        return userDto;

    }
}
