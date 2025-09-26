package com.gzu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication()
@EnableWebMvc
//@MapperScan(value = "com.gzu.mapper")
public class ServeApplication {


    public static void main(String[] args) {
        SpringApplication.run(ServeApplication.class, args);
    }

}
