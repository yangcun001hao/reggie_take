package com.guole.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guole.reggie.entity.OrderDetail;
import com.guole.reggie.mapper.OrderDetailMapper;
import com.guole.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author 郭乐
 * @date 2022/10/21
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
