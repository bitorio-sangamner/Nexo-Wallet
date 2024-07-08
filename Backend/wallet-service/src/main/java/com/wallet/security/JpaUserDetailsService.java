package com.wallet.security;

import com.wallet.entities.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.ArrayList;

@Component
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private RestClient restClient;

    @Autowired
    AuthUser authUser;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        authUser= restClient.get()
                .uri("http://localhost:9090/getUser/{email}",username)
                .header("From-Gateway", "true")
                .retrieve()
                .body(AuthUser.class);

        if (authUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new org.springframework.security.core.userdetails.User(authUser.getEmail(), authUser.getPassword(), new ArrayList<>());

    }

    public AuthUser getUserDetails()
    {
        return authUser;
    }
}
