package com.example.springbatchscheduling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBatchSchedulingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchSchedulingApplication.class, args);
    }

}
