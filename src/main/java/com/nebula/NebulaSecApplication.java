package com.nebula;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync // 开启异步任务，配合虚拟线程使用
@SpringBootApplication
public class NebulaSecApplication {
    public static void main(String[] args) {
        SpringApplication.run(NebulaSecApplication.class, args);
    }
}
