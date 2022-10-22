package com.guole.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guole.reggie.common.R;
import com.guole.reggie.dto.DishDto;
import com.guole.reggie.entity.Category;
import com.guole.reggie.entity.Dish;
import com.guole.reggie.entity.DishFlavor;
import com.guole.reggie.service.CategoryService;
import com.guole.reggie.service.DishFlavorService;
import com.guole.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 * @author 郭乐
 * @date 2022/10/17
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return {@link R}<{@link String}>
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info("添加菜品：{}",dishDto);
        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }


    /**
     * 修改菜品
     *
     * @param dishDto 菜dto
     * @return {@link R}<{@link String}>
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info("修改菜品：{}",dishDto);
        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功");
    }

    /**
     * 菜品信息的分类查询
     *
     * @param page     页面
     * @param pageSize 页面大小
     * @param name     名字
     * @return {@link R}<{@link Page}>
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) ->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId(); //获取分类id
            Category byId = categoryService.getById(categoryId);
            if (byId != null) {
                String name1 = byId.getName();
                dishDto.setCategoryName(name1);
                log.info("分类名字是：{}",name1);
            }

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     *
     * @param id id
     * @return {@link R}<{@link DishDto}>
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto byIdWithFlavor = dishService.getByIdWithFlavor(id);
        return R.success(byIdWithFlavor);
    }

    /**
     * 根据条件查询餐品数据
     *
     * @param dish 菜
     * @return {@link R}<{@link List}<{@link Dish}>>
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());

        //查询状态等于1的，即在售卖的
        wrapper.eq(Dish::getStatus,1);
        //添加排序条件
        wrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(wrapper);
        List<DishDto> dtoList  = list.stream().map((item) ->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId(); //获取分类id
            Category byId = categoryService.getById(categoryId);
            if (byId != null) {
                String name1 = byId.getName();
                dishDto.setCategoryName(name1);
            }

            Long id = item.getId();//菜品id
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId,id);
            List<DishFlavor> list1 = dishFlavorService.list(queryWrapper);
            dishDto.setFlavors(list1);

            return dishDto;
        }).collect(Collectors.toList());;
        return R.success(dtoList);
    }

}
