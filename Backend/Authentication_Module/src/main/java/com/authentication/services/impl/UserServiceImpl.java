package com.authentication.services.impl;

import com.authentication.entities.User;
import com.authentication.payloads.UserDto;
import com.authentication.repositories.UserRepository;
import com.authentication.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.authentication.exceptions.*;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
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
