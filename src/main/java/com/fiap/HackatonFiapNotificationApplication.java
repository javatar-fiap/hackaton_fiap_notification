package com.fiap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.fiap.hackatonfiapnotification")
public class HackatonFiapNotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(HackatonFiapNotificationApplication.class, args);
    }
}
