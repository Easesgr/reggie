package com.anyi.reggie.service;

import com.anyi.reggie.dto.DishDto;
import com.anyi.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜品管理 服务类
 * </p>
 *
 * @author anyi
 * @since 2022-05-24
 */
public interface DishService extends IService<Dish> {

    // 添加菜品
    void addDish(DishDto dishDto);

    Page<DishDto> pageSearch(int page, int pageSize, String name);

    DishDto getDishById(Long id);

    void updateDish(DishDto dishDto);

    void deleteDish(String  ids);

    List<DishDto> getList(Long categoryId, Integer status);
}
