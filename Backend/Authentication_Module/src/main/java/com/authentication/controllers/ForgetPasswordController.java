package com.authentication.controllers;

import com.alibaba.fastjson.JSONObject;
import com.authentication.payloads.SetPasswordDto;
import com.authentication.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Nexousers")
public class ForgetPasswordController {
    private static final Logger logger = LoggerFactory.getLogger(ForgetPasswordController.class);
    @Autowired
    private UserService userService;

    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody JSONObject json) throws MessagingException, Exception {

            String email = json.getString("email");
            String message = userService.forgotPassword(email);

            if ("please check your email to set a new password for your account".equals(message)) {
                logger.info("Forgot password request processed successfully for email: {}", email);
                return ResponseEntity.ok(message);
            }
            else {
                logger.warn("Failed to process forgot password request for email: {}. Reason: {}", email, message);
                return ResponseEntity.badRequest().body(message);
            }
        }

    @GetMapping("/forgetPassword-page")
    public String showForgetPasswordPage()throws Exception
    {
        logger.info("inside showForgetPasswordPage");
        return "forgetPassword";
    }

    @PutMapping("/setNew-password")
    public ResponseEntity<String> setPassword(@Valid @RequestBody SetPasswordDto setPasswordDto) throws Exception{

            String email = setPasswordDto.getEmail();
            String newPassword = setPasswordDto.getNewPassword();

            String message = userService.setPassword(email, newPassword);

            HttpStatus status;
            if (message.equals("New password set successfully login with new password")) {
                status = HttpStatus.OK;
            }
            else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

            return ResponseEntity.status(status).body(message);
    }
}
