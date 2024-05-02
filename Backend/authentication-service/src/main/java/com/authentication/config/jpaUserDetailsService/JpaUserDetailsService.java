package com.authentication.config.jpaUserDetailsService;

import com.authentication.dao.AuthUserRepository;
import com.authentication.exceptions.EmailNotRegisteredException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return authUserRepository
                .findByEmail(email)
                .map(SecurityUser::new)
                .orElseThrow(() -> new EmailNotRegisteredException("Email is not registered.", HttpStatus.CONFLICT));
    }
}
