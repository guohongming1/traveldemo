package com.guo.traveldemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Configuration
@EnableTransactionManagement
@MapperScan({"com.guo.traveldemo.web.mapper"})
public class TraveldemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TraveldemoApplication.class, args);
    }

}
