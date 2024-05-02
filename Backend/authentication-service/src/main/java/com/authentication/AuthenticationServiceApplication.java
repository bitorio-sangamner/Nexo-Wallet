package com.authentication;

import com.authentication.dao.AuthUserRepository;
import com.authentication.entities.AuthUser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class AuthenticationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(AuthUserRepository user) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return args -> {
            boolean authUser = user.findByEmail("admin@yopmail.com").isPresent();
            if (!authUser) {
                user.save(AuthUser
                        .builder()
                        .email("admin@yopmail.com")
                        .password(passwordEncoder.encode("password"))
                        .pin(1234567)
                        .isVerified(true)
                        .roles("ADMIN")
                        .build()
                );
            }
        };
    }

}
