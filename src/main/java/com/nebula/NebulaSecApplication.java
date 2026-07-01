package com.nebula;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching
@SpringBootApplication
@MapperScan("com.nebula.mapper")
public class NebulaSecApplication {
    public static void main(String[] args) {
        SpringApplication.run(NebulaSecApplication.class, args);
    }
}
