package com.neuedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
@MapperScan(value = "com.neuedu.mapper")
public class WeiweiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeiweiApplication.class, args);
    }


}

