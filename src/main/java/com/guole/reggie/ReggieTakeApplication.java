package com.guole.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 郭乐
 */
@Slf4j  //生成日志
@SpringBootApplication
@ServletComponentScan   //扫描过滤器
@EnableTransactionManagement    //开启事务注解
public class ReggieTakeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieTakeApplication.class, args);
        log.info("项目启动成功");
    }

}
