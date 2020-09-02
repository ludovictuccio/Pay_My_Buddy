package com.paymybuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PayMyBuddyApplication class that contain method main.
 *
 * @author Ludovic Tuccio
 */
@SpringBootApplication
//@Configuration
//@EncryptablePropertySource(name = "EncryptedProperties", value = "classpath:application.properties")
public class PayMyBuddyApplication {

    /**
     * Application method main.
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(PayMyBuddyApplication.class, args);
    }

}
