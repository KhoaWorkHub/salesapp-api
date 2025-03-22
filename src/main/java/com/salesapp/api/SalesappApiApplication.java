package com.salesapp.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SalesappApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesappApiApplication.class, args);
    }

}
