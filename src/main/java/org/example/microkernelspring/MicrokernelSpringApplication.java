package org.example.microkernelspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MicrokernelSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrokernelSpringApplication.class, args);
    }

}
