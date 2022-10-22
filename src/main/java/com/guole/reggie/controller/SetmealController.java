package com.guole.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.guole.reggie.common.R;
import com.guole.reggie.dto.SetmealDto;
import com.guole.reggie.entity.Category;
import com.guole.reggie.entity.Setmeal;
import com.guole.reggie.service.CategoryService;
import com.guole.reggie.service.SetmealDishService;
import com.guole.reggie.service.SetmealService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 * @author 郭乐
 * @date 2022/10/18
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;


    /**
     * 新增套餐
     *
     * @param setmealDto setmeal dto
     * @return {@link R}<{@link String}>
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){

        setmealService.saveWithDish(setmealDto);

        return R.success("套餐添加成功");
    }


    /**
     * 套菜分页查询
     *
     * @param page     页面
     * @param pageSize 页面大小
     * @param name     名字
     * @return {@link R}<{@link Page}>
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> page1 = new Page<>();
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        wrapper.like(name != null,Setmeal::getName,name);
        //添加排序条件
        wrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,wrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,page1,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) ->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //查询分类
            Category byId = categoryService.getById(categoryId);
            if (byId != null) {
                String name1 = byId.getName();
                setmealDto.setCategoryName(name1);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        page1.setRecords(list);

        return R.success(page1);
    }

    /**
     * 批量删除套菜
     *
     * @param ids id
     * @return {@link R}<{@link String}>
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.removeWithDish(ids);


        return R.success("套餐数据删除成功");
    }

    /**
     * 根据条件查询套餐
     *
     * @param setmeal setmeal
     * @return {@link R}<{@link List}<{@link Setmeal}>>
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        wrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(wrapper);

        return R.success(list);
    }




}
