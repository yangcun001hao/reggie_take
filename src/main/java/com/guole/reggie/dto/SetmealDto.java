package com.guole.reggie.dto;

import com.guole.reggie.entity.Setmeal;
import com.guole.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

/**
 * @author 郭乐
 */
@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
