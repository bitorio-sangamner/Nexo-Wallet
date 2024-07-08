package com.wallet.controller;

import com.wallet.security.JpaUserDetailsService;
import com.wallet.service.SubUserService;
import com.wallet.service.UserWalletService;
import com.wallet.util.RandomStringGenerator;
import com.wallet.util.SubUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

//    @PostMapping("/createSubUserManually")
//    public String createSubUserManually()
//    {
//        AuthUser user=jpaUserDetailsService.getUserDetails();
//        log.info("user id :"+user.getId());
//        log.info("email :"+user.getEmail());
//        log.info("password :"+user.getPassword());
//
//        String password=RandomStringGenerator.generateRandomString(8, 30);
//        log.info("password :"+password);
//
//        Map<String, Object> subUserOnBybit=subUserService.createSubUserOnBybit(user.getEmail(),password);
//
//        // Print all entries in the map (optional, for debugging purposes)
//        for (Map.Entry<String, Object> entry : subUserOnBybit.entrySet()) {
//            System.out.println(entry.getKey() + " - " + entry.getValue());
//        }
//
//        if(subUserOnBybit!=null) {
//            if (subUserOnBybit.containsKey("result")) {
//
//                Map<String, Object> result = (Map<String, Object>) subUserOnBybit.get("result");
//
//                if (result != null && result.containsKey("uid")) {
//                    String subUserId = result.get("uid").toString();
//                    return this.userWalletService.createWallet((long) user.getId(), user.getEmail(), subUserId);
//                }
//            }
//        }
//        return "Hello";
//    }
}
