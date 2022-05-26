package com.anyi.reggie.service.impl;

import com.anyi.reggie.common.CustomException;
import com.anyi.reggie.common.R;
import com.anyi.reggie.entity.Category;
import com.anyi.reggie.entity.Dish;
import com.anyi.reggie.entity.Setmeal;
import com.anyi.reggie.mapper.CategoryMapper;
import com.anyi.reggie.service.CategoryService;
import com.anyi.reggie.service.DishService;
import com.anyi.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 菜品及套餐分类 服务实现类
 * </p>
 *
 * @author anyi
 * @since 2022-05-24
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private DishService dishService;

    @Resource
    private SetmealService setmealService;

    @Override
    public void delete(Long ids) {
        // 查询是否有与该分类关联的菜品和套餐
        List<Dish> dishes = dishService.list(new QueryWrapper<Dish>().eq("category_id", ids));
        if (dishes.size() > 0){
            throw new CustomException("有已管理的菜品，无法删除");
        }
        List<Setmeal> setmeals = setmealService.list(new QueryWrapper<Setmeal>().eq("category_id", ids));
        if (setmeals.size()> 0){
            throw new CustomException("有已管理的套餐，无法删除");
        }
        removeById(ids);
    }
}
