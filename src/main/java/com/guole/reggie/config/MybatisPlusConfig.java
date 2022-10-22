package com.guole.reggie.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 郭乐
 * @date 2022/10/15
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * mybatis +拦截器
     * mybatisPlus的拦截器
     * @return {@link MybatisPlusInterceptor}
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor my = new MybatisPlusInterceptor();
        my.addInnerInterceptor(new PaginationInnerInterceptor());
        return my;

    }

}
