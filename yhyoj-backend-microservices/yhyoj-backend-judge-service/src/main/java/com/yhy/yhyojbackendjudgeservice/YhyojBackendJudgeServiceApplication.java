package com.yhy.yhyojbackendjudgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.yhy")
public class YhyojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YhyojBackendJudgeServiceApplication.class, args);
    }

}