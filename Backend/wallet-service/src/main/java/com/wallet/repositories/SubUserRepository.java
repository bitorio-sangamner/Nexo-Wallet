package com.wallet.repositories;

import com.wallet.entities.SubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface SubUserRepository extends JpaRepository<SubUser,Integer> {

    SubUser findByUsername(String userName);
    SubUser findByEmail(String email);
}
