package com.example.miniproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example.miniproject")
@EntityScan(basePackages = {"com.example.miniproject.entity"})
@EnableJpaRepositories(basePackages = {"com.example.miniproject.repo"})
public class MiniProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiniProjectApplication.class, args);
    }
}
