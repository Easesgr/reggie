package com.anyi.reggie.service.impl;

import com.anyi.reggie.common.CommentRedis;
import com.anyi.reggie.dto.DishDto;
import com.anyi.reggie.entity.Dish;
import com.anyi.reggie.entity.DishFlavor;
import com.anyi.reggie.mapper.DishMapper;
import com.anyi.reggie.service.CategoryService;
import com.anyi.reggie.service.DishFlavorService;
import com.anyi.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 *
 * @author anyi
 * @since 2022-05-24
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Resource
    private DishFlavorService dishFlavorService;

    @Resource
    private CategoryService categoryService;


    @Resource
    private RedisTemplate redisTemplate;
    /**
     * 添加菜品
     * @param dishDto
     */
    @Override
    @Transactional
    public void addDish(DishDto dishDto) {
        // 首先封装菜品信息并保存
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto, dish);
        save(dish);

        List<DishFlavor> flavors = dishDto.getFlavors();
        // 封装flavors信息并且批量保存
        flavors =flavors.stream().map((item)->{
            item.setDishId(dish.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public Page<DishDto> pageSearch(int page, int pageSize, String name) {
        // 创建分页
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        // 返回页面的分页
        Page<DishDto> dishDtoPage = new Page<>();

        QueryWrapper<Dish> employeeQueryWrapper = new QueryWrapper<>();

        employeeQueryWrapper.orderByDesc("create_time");
        if(name !=null){
            employeeQueryWrapper.like("name", name);
        }
        // 查出信息
        page(pageInfo, employeeQueryWrapper);
        // 拷贝信息
        BeanUtils.copyProperties(pageInfo, dishDtoPage);

        // 根据id查询出菜品名
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> recordsDto = records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            String categoryName = categoryService.getById(item.getCategoryId()).getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        // 重新放入到 返回集合中
        dishDtoPage.setRecords(recordsDto);
        return dishDtoPage;
    }

    /**
     * 根据id回显数据
     * @param id
     * @return
     */
    @Override
    public DishDto getDishById(Long id) {
        // 根据id查询菜品信息
        DishDto dishDto = new DishDto();
        Dish dish = getById(id);
        // 根据id查询口味信息
        List<DishFlavor> dishFlavors = dishFlavorService.list(new QueryWrapper<DishFlavor>().eq("dish_id", id));

        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(dishFlavors);
        // 返回数据
        return dishDto;
    }

    /**
     * 更新菜品
     * @param dishDto
     */
    @Override
    public void updateDish(DishDto dishDto) {
        // 首先封装菜品信息并保存
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto, dish);
        updateById(dish);

        List<DishFlavor> flavors = dishDto.getFlavors();
        // 封装flavors信息并且批量保存
        flavors =flavors.stream().map((item)->{
            item.setDishId(dish.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.updateBatchById(flavors);
    }

    /**
     * 根据id删除菜品
     * @param ids
     */
    @Override
    public void deleteDish(String  ids) {
        String[] list = ids.split(",");
        for (String id : list) {
            // 删除菜品
            removeById(Long.parseLong(id));
            // 删除菜品对应口味信息
            dishFlavorService.remove(new QueryWrapper<DishFlavor>().eq("dish_id", id));
        }
    }

    @Override
    public List<DishDto> getList(Long categoryId, Integer status) {

        String key = CommentRedis.DISH_PREFIX + categoryId + "_" + status;

        List<DishDto> dtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if (dtoList !=null){
            return dtoList;
        }
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, categoryId).eq(Dish::getStatus,status);
        List<Dish> list = list(wrapper);
        List<DishDto> dishDtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            String categoryName = categoryService.getById(item.getCategoryId()).getName();
            dishDto.setCategoryName(categoryName);
            List<DishFlavor> favors = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, item.getId()));
            dishDto.setFlavors(favors);
            return dishDto;
        }).collect(Collectors.toList());
        redisTemplate.opsForValue().set(key, dishDtoList,30, TimeUnit.MINUTES);
        return dishDtoList;
    }
}
