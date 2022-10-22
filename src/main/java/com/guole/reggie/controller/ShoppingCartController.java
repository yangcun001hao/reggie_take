package com.guole.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guole.reggie.common.BaseContext;
import com.guole.reggie.common.R;
import com.guole.reggie.entity.ShoppingCart;
import com.guole.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 郭乐
 * @date 2022/10/21
 */
@Slf4j
@RequestMapping("/shoppingCart")
@RestController
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 添加购物车
     *
     * @param shoppingCart 购物车
     * @return {@link R}<{@link ShoppingCart}>
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        //设置用户id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,currentId);
        if (dishId != null) {
            //添加的是菜品
            wrapper.eq(ShoppingCart::getDishId,dishId);
        } else {
            //
            wrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(wrapper);

        if (one != null) {
            //已经存在，数量加一
            Integer number = one.getNumber();
            one.setNumber( number+ 1);
            shoppingCartService.updateById(one);
        }else {
            //不存在，添加
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }

        return R.success(one);
    }

    /**
     * 查看购物车
     *
     * @return {@link R}<{@link List}<{@link ShoppingCart}>>
     */
    @GetMapping("list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        wrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(wrapper);

        return R.success(list);
    }

    /**
     * 清空购物车
     *
     * @return {@link R}<{@link String}>
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(wrapper);
        return R.success("清空成功");
    }
}
