package com.guole.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guole.reggie.common.CustomException;
import com.guole.reggie.dto.SetmealDto;
import com.guole.reggie.entity.Setmeal;
import com.guole.reggie.entity.SetmealDish;
import com.guole.reggie.mapper.SetmealMapper;
import com.guole.reggie.service.SetmealDishService;
import com.guole.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 郭乐
 * @date 2022/10/15
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套菜，同时需要保存套菜和菜品的关联关系
     *
     * @param setmealDto setmeal dto
     */
    @Transactional //事务注解
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套菜的基本信息
        this.save(setmealDto);
        List<SetmealDish> li = setmealDto.getSetmealDishes();
        li.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套菜的关联信息
        setmealDishService.saveBatch(li);
    }

    /**
     * 删除套菜，同时删除套餐和菜品的关联信息
     *
     * @param ids id
     */
    @Override
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态，确认是否可以删除
        //不能删除，抛出异常信息
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId,ids).eq(Setmeal::getStatus,1);
        int count = this.count(wrapper);
        if (count > 0 ) {
            throw new CustomException("套餐正在售卖中，不能售卖");
        }

        //如果可以删除，先删除套餐中的数据
        this.removeByIds(ids);
        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(wrapper1);

    }
}
