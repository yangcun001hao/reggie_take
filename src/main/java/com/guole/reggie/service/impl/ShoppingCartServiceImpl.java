package com.guole.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guole.reggie.entity.ShoppingCart;
import com.guole.reggie.mapper.ShoppingCartMapper;
import com.guole.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author 郭乐
 * @date 2022/10/21
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
