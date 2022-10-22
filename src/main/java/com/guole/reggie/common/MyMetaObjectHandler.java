package com.guole.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元数据对象处理器
 * @author 郭乐
 * @date 2022/10/15
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 执行插入时执行
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        //将存放在ThreadLocal域中的用户id取出来
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());

    }

    /**
     * 更新填充时执行
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {

        //获得线程id，三个线程id是一样的
        long threadId = Thread.currentThread().getId();
        log.info("common MyMetaObjectHandler 类中 线程id为：{}",threadId);

        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
