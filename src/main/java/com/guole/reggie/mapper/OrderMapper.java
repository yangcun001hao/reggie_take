package com.guole.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guole.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 郭乐
 * @date 2022/10/21
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
