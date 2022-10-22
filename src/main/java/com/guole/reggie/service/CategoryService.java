package com.guole.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guole.reggie.entity.Category;

/**
 * @author 郭乐
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
