package com.yhy.yhyojbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.yhy.yhyojbackenduserservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.yhy")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.yhy.yhyojbackendserviceclient.service"})
public class YhyojBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YhyojBackendUserServiceApplication.class, args);
    }

}
