package com.guole.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guole.reggie.dto.DishDto;
import com.guole.reggie.entity.Dish;

/**
 * @author 郭乐
 */
public interface DishService extends IService<Dish> {
    /**
     * 保存与味道
     * 新增菜品，同时插入菜品对象的口味数据，需要操作两张表：dish、dish_flavor
     * @param dishDto 菜dto
     */
    public void saveWithFlavor(DishDto dishDto);

    /**
     * 通过id查询菜品信息和口味信息
     *
     * @param id id
     * @return {@link DishDto}
     */
    public DishDto getByIdWithFlavor(Long id);


    /**
     * 更新菜品信息，同时适应对应的口味信息
     *
     * @param dishDto 菜dto
     */
    public void updateWithFlavor(DishDto dishDto);

}
