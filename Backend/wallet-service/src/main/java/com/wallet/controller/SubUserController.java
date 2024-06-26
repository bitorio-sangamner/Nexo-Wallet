package com.wallet.controller;

import com.wallet.service.SubUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.Map;

@RestController
@RequestMapping("/api/subUser")
@Slf4j
public class SubUserController {

    @Autowired
    private SubUserService subUserService;
    @PostMapping("/create/{userId}/{email}/{password}")
    public String createSubUserOnBybit(@PathVariable Long userId, @PathVariable String email, @PathVariable String password)
    {
        log.info("inside createSubUserOnBybit create...........");

        Map<String, Object> subUserOnBybit=subUserService.createSubUserOnBybit(email,password);

        // Print all entries in the map (optional, for debugging purposes)
        for (Map.Entry<String, Object> entry : subUserOnBybit.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }

        if(subUserOnBybit!=null) {
            if (subUserOnBybit.containsKey("uid")) {
                String subUserId = (String) subUserOnBybit.get("uid");
            }
        }
        return null;
    }
}
