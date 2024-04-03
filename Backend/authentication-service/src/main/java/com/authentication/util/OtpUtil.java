package com.authentication.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpUtil {

    public String generateOtp()
    {
        Random random=new Random();
        int randomNumber=random.nextInt(999999);
        //randomNumber=001234

        String output=Integer.toString(randomNumber);
        //output=1234
        while(output.length()<6)
        {
            output="0"+output;
        }
        //output=001234
        return output;
    }
}
