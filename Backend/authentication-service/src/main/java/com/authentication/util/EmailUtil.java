package com.authentication.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class EmailUtil {

    private JavaMailSender javaMailSender;

    public void sendResetPasswordEmail(String email) {
         try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Reset Password Link");
            mimeMessageHelper.setFrom("no-reply@nexo.wallet");
            String text = """
                    <div>
                       <a href="http://localhost:8383/reset-password?email=%s" target="_blank">Click link to set password</a>
                    </div>
                    """;

            mimeMessageHelper.setText(text.formatted(email), true);

            javaMailSender.send(mimeMessage);
         } catch (MessagingException e) {
             throw new RuntimeException("Internal Server error");
         }
    }

    public void sendVerifyEmail(String email, String token) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Verify Email");
            mimeMessageHelper.setFrom("no-reply@nexo.wallet");
            String text = """
                    <div>
                     <a href="http://localhost:8383/verify?email=%s&token=%s" target="_blank">Click here to Verify</a>
                    </div>
                    """;

            mimeMessageHelper.setText(text.formatted(email, token), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Internal Server error");
        }
    }
}
