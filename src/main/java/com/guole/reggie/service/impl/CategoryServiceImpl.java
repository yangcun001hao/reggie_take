package com.guole.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guole.reggie.common.CustomException;
import com.guole.reggie.entity.Category;
import com.guole.reggie.entity.Dish;
import com.guole.reggie.entity.Setmeal;
import com.guole.reggie.mapper.CategoryMapper;
import com.guole.reggie.service.CategoryService;
import com.guole.reggie.service.DishService;
import com.guole.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 郭乐
 * @date 2022/10/15
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /**
     * 删除
     * 根据id删除分类，删除之前进行判断
     * @param id id
     */
    @Override
    public void remove(Long id) {
        //判断当前分类是否关联了菜品，关联抛出业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0) {
            //已经关联菜品，抛出业务异常
            throw new CustomException("当前分类下关联了菜品，无法删除");
        }
        //判断当前分类是否关联了套餐，关联抛出业务异常
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(lambdaQueryWrapper);
        if (count2 > 0) {
            //已经关联了套餐，抛出业务异常
            throw new CustomException("当前分类下关联了套餐，无法删除");

        }
        //都没关联，正常删除
        super.removeById(id);
    }
}
