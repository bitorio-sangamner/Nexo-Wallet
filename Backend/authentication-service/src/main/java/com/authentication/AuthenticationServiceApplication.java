package com.authentication;

import com.authentication.dao.AuthUserRepository;
import com.authentication.entities.AuthUser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;


@SpringBootApplication
public class AuthenticationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(AuthUserRepository user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return args -> {
            Optional<AuthUser> authUser = user.findByEmail("admin@yopmail.com");
            if (authUser.isEmpty()) {
                authUser = Optional.ofNullable(AuthUser.builder()
                        .email("admin@yopmail.com")
                        .password(passwordEncoder.encode("Password@1234"))
                        .pin(123456)
                        .isVerified(true)
                        .roles("ADMIN")
                        .build());
            }
            authUser.get().setLoggedIn(false);
            user.save(authUser.get());
        };
    }
}