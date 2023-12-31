package com.ll.gong9ri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableJpaAuditing
@EnableScheduling
public class Gong9riApplication {
    public static void main(String[] args) {
        SpringApplication.run(Gong9riApplication.class, args);
    }
}
