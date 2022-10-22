package com.guole.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guole.reggie.common.R;
import com.guole.reggie.entity.Category;
import com.guole.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类的管理
 * @author 郭乐
 * @date 2022/10/15
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category 类别
     * @return {@link R}<{@link String}>
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {

        categoryService.save(category);
        return R.success("新增分类成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);

        //进行分页查询
        categoryService.page(pageInfo,wrapper);
        return R.success(pageInfo);
    }

    /**
     * 通过id删除分类
     *
     * @param ids ids
     * @return {@link R}<{@link String}>
     */
    @DeleteMapping
    public R<String> deleteById(Long ids){
        log.info("删除的分类的id为：{}",ids);

        //categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("删除成功");
    }

    /**
     * 根据id更新分类信息
     *
     * @param category 类别
     * @return {@link R}<{@link String}>
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改的信息为：{}",category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功") ;
    }

    /**
     * 根据条件来查询分类数据
     *
     * @param category 类别
     * @return {@link R}<{@link List}<{@link Category}>>
     */
    @GetMapping("/list")
    public R<List<Category>> listR(Category category) {
        log.info("查询请求为：{}",category);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }

}
