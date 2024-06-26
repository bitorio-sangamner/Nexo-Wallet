package com.authentication.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * This class is used for sending all types of email to the provided email address
 *
 * @author rsmalani
 */
@Component
@AllArgsConstructor
@Slf4j
public class EmailUtil {

    /**
     * JavaMailSender field for using jakarta mail for sending email to the user email address.
     */
    private JavaMailSender javaMailSender;

    /**
     * This method is used for sending reset-password link to the user's provided email address for the user starting
     * the forgot password routine.
     *
     * @param email user's email address
     */
    public void sendResetPasswordEmail(String email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Reset Password Link");
            mimeMessageHelper.setFrom("no-reply@nexo.wallet");
            String text = """
                    <div>
                       <a href="http://localhost:4200/resetpassword?email=%s" target="_blank">Click link to set password</a>
                    </div>
                    """;

            mimeMessageHelper.setText(text.formatted(email), true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Internal Server error");
        }
    }

    /**
     * This method is used for sending verify-email link to the user's provided email address for the user to verify
     * email address in the system.
     *
     * @param email user's email address
     * @param token verification token
     */
    public void sendVerifyEmail(String email, String token) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Verify Email");
            mimeMessageHelper.setFrom("no-reply@nexo.wallet");
            String text = """
                    <div>
                     <a href="http://localhost:4200/verify?email=%s&token=%s" target="_blank">Click here to Verify</a>
                    </div>
                    """;

            mimeMessageHelper.setText(text.formatted(email, token), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Internal Server error");
        }
    }
}
