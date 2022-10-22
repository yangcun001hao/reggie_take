package com.guole.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guole.reggie.dto.DishDto;
import com.guole.reggie.entity.Dish;
import com.guole.reggie.entity.DishFlavor;
import com.guole.reggie.mapper.DishMapper;
import com.guole.reggie.service.DishFlavorService;
import com.guole.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.context.ThemeSource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 郭乐
 * @date 2022/10/15
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品，同时保存对应的口味数据
     * 由于设计多表添加，需要在添加事务
     *
     * @param dishDto 菜dto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品基本信息到dish中
        this.save(dishDto);

        //菜品id；
        Long id = dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        //将集合中的数据进行赋值
        flavors = flavors.stream().map((item) -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味
        dishFlavorService.saveBatch(flavors);


    }

    /**
     * 通过id查询菜品信息和口味信息
     *
     * @param id id
     * @return {@link DishDto}
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息
        Dish byId = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(byId,dishDto);

        //查询当前菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor>  wrapper= new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,byId.getId());
        List<DishFlavor> list = dishFlavorService.list(wrapper);

        dishDto.setFlavors(list);
        return dishDto;
    }

    /**
     * 更新菜品信息，同时适应对应的口味信息
     *
     * @param dishDto 菜dto
     */
    @Override
//    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);

        //清理当前菜品口味数据
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper();
        wrapper.eq(DishFlavor::getDishId,dishDto.getId());
        boolean remove = dishFlavorService.remove(wrapper);
        log.info("删除成功：{}",remove);


        //再添加当前口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }
}
