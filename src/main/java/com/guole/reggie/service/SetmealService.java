package com.guole.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guole.reggie.dto.SetmealDto;
import com.guole.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author 郭乐
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套菜，同时需要保存套菜和菜品的关联关系
     *
     * @param setmealDto setmeal dto
     */
    public void saveWithDish(SetmealDto setmealDto);


    /**
     * 删除套菜，同时删除套餐和菜品的关联信息
     *
     * @param ids id
     */
    public void removeWithDish(List<Long> ids);
}
