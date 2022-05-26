package com.anyi.reggie.dto;


import com.anyi.reggie.entity.Setmeal;
import com.anyi.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
