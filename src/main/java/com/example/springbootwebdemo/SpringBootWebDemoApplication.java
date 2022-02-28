package com.example.springbootwebdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * spring boot app启动类
 *
  * @date 2022-01-01
 */
@EnableCaching
@EnableAsync
@EnableJms
@EnableScheduling
@SpringBootApplication
public class SpringBootWebDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebDemoApplication.class, args);
    }

}
