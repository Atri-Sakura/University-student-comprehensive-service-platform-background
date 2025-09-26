package com.gzu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TestSpringBootApplication3 {
    public static void main(String[] args) {
        SpringApplication.run(TestSpringBootApplication3.class);
    }
}