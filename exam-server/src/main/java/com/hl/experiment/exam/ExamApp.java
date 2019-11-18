package com.hl.experiment.exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication()
//@SpringBootApplication(scanBasePackages = {"com.hl.experiment.exam", "com.hl.experiment.security"})
public class ExamApp {
    public static void main(String[] args) {
        SpringApplication.run(ExamApp.class, args);
    }
}
