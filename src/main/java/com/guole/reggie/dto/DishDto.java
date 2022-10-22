package com.guole.reggie.dto;

import com.guole.reggie.entity.Dish;
import com.guole.reggie.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 郭乐
 */
@Data
public class DishDto extends Dish {

    //菜品口味数据
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
