package com.guole.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guole.reggie.entity.Orders;

/**
 * @author 郭乐
 * @date 2022/10/21
 */
public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     *
     * @param orders 订单
     */
    public void submit(Orders orders);
}
