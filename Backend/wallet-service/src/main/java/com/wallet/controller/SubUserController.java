package com.wallet.controller;

import com.wallet.entities.AuthUser;
import com.wallet.payloads.SubUserDto;
import com.wallet.security.JpaUserDetailsService;
import com.wallet.service.SubUserService;
import com.wallet.service.UserWalletService;
import com.wallet.util.RandomStringGenerator;
import com.wallet.util.SubUserApiKeyRequest;
import com.wallet.util.SubUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/subUser")
@Slf4j
public class SubUserController {

    @Autowired
    private SubUserService subUserService;

    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private JpaUserDetailsService jpaUserDetailsService;

    @PostMapping("/create/{userId}/{email}")
    public String createSubUserOnBybit(@PathVariable Long userId, @PathVariable String email) {
        log.info("inside createSubUserOnBybit create...........");
        String password = RandomStringGenerator.generateRandomString(8, 30);
        SubUserResponse subUserResponse = subUserService.createSubUserOnBybit(email, password);

        if (subUserResponse != null && subUserResponse.getResult() != null) {
            SubUserResponse.Result result = subUserResponse.getResult();

            if (result != null && result.getUid() != null) {
                String subUserId = result.getUid();
                return this.userWalletService.createWallet(userId, email, subUserId);
            }
        }
        return "Sub-user creation failed";
    }

    @PostMapping("/createSubUserManually")
    public String createSubUserManually() {
        // Get the email from SecurityContextHolder
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // Fetch user details using the email
        AuthUser user = jpaUserDetailsService.getUserDetails(email);
        log.info("user id :" + user.getId());
        log.info("email :" + user.getEmail());
        log.info("password :" + user.getPassword());

        String password = RandomStringGenerator.generateRandomString(8, 30);
        log.info("password :" + password);

        SubUserResponse subUserResponse = subUserService.createSubUserOnBybit(user.getEmail(), password);

        if (subUserResponse != null && subUserResponse.getResult() != null) {
            SubUserResponse.Result result = subUserResponse.getResult();

            if (result != null && result.getUid() != null) {
                String subUserId = result.getUid();
                return this.userWalletService.createWallet((long) user.getId(), user.getEmail(), subUserId);
            }
        }
        return "Sub-user creation failed";
    }


    @PostMapping("/createSubUserApiKeyOnBybit")
    public String createSubUserApiKeyOnBybit(@RequestBody SubUserApiKeyRequest subUserApiKeyRequest) throws Exception {
        // Get the email from SecurityContextHolder
        //String email = SecurityContextHolder.getContext().getAuthentication().getName();
        //log the received data
        log.info("Note: {}", subUserApiKeyRequest.getNote());
        log.info("ReadOnly: {}", subUserApiKeyRequest.getReadOnly());
        log.info("Permissions: {}", subUserApiKeyRequest.getPermissions());

        //SubUserDto subUserDto=subUserService.getSubUserByUserName(email);
        //subUserApiKeyRequest.setSubuid(Integer.parseInt((subUserDto.getUserId())));
        String msg=subUserService.createSubUserApiKey(subUserApiKeyRequest);

        return msg;
    }
}
