package com.lushunde.springboot.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
public class StartBootMain {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(StartBootMain.class, args);
    }
}