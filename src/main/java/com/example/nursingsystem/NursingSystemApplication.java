package com.example.nursingsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.example.nursingsystem.**.mapper")
@SpringBootApplication
public class NursingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(NursingSystemApplication.class, args);
    }

}
