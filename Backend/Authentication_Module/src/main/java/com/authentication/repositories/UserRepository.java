package com.authentication.repositories;

import com.authentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    //custom finder methods
    User findByEmail(String email);

}
