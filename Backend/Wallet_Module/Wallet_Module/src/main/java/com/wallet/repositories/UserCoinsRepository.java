package com.wallet.repositories;

import com.wallet.entites.UserCoinsDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCoinsRepository extends JpaRepository<UserCoinsDetails,Integer> {

    //custom methods
    List<UserCoinsDetails> findAllByUserId(Long userId);
}
