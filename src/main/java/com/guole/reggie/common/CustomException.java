package com.guole.reggie.common;

/**
 * 自定义异常
 * @author 郭乐
 * @date 2022/10/15
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
