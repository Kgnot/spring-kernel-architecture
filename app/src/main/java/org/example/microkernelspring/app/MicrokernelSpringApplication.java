package org.example.microkernelspring.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(
        scanBasePackages = "org.example.microkernelspring"
)
@EnableScheduling
@EntityScan(basePackages = "org.example.microkernelspring")
@EnableJpaRepositories(basePackages = "org.example.microkernelspring")
public class MicrokernelSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrokernelSpringApplication.class, args);
    }
}