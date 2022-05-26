package com.anyi.reggie.service.impl;

import com.anyi.reggie.dto.SetmealDto;
import com.anyi.reggie.entity.Category;
import com.anyi.reggie.entity.Setmeal;
import com.anyi.reggie.entity.SetmealDish;
import com.anyi.reggie.mapper.SetmealMapper;
import com.anyi.reggie.service.CategoryService;
import com.anyi.reggie.service.SetmealDishService;
import com.anyi.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐 服务实现类
 * </p>
 *
 * @author anyi
 * @since 2022-05-24
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Resource
    private SetmealDishService service;

    @Resource
    private CategoryService categoryService;

    /**
     * 添加套餐信息
     * @param setmealDto
     */
    @Override
    @Transactional
    public void add(SetmealDto setmealDto) {
        // 将套餐信息保存
        save(setmealDto);
        // 将套餐含有菜品信息保存
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(item->{
            item.setSetmealId(setmealDto.getId().toString());
            return item;
        }).collect(Collectors.toList());
        service.saveBatch(setmealDishes);
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Page<SetmealDto> getPage(Integer page, Integer pageSize, String name) {
        // 分页查询
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);

        Page<SetmealDto> setmealDtoPage = new Page<>();
        // 添加条件查询
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        if (name != null){
            wrapper.like(Setmeal::getName, name);
        }
        page(setmealPage,wrapper);
        // 封装dto
        BeanUtils.copyProperties(setmealPage, setmealDtoPage);
        // 对分类名称和套餐菜单封装
        List<SetmealDto> records = setmealPage.getRecords().stream().map(item->{
            SetmealDto setmealDto = new SetmealDto();
            // 设置分类
            String categroyName = categoryService.getById(item.getCategoryId()).getName();
            setmealDto.setCategoryName(categroyName);
            // 查询出所有的dish
            LambdaQueryWrapper<SetmealDish> wr = new LambdaQueryWrapper<>();
            wr.eq(SetmealDish::getSetmealId, item.getId());
            List<SetmealDish> list = service.list(wr);
            setmealDto.setSetmealDishes(list);
            BeanUtils.copyProperties(item, setmealDto);
            return setmealDto;
        }).collect(Collectors.toList());
        // 放到dto分页数据中
        setmealDtoPage.setRecords(records);
        return setmealDtoPage;
    }

    /**
     * 根据id查询套餐信息
     * @param ids
     * @return
     */
    @Override
    public SetmealDto getSetmeal(Long ids) {
        // 根据id查询出套餐信息
        Setmeal setmeal = getById(ids);
        // 查询出套餐菜品信息
        List<SetmealDish> list = service.list(new LambdaQueryWrapper<SetmealDish>().eq(SetmealDish::getSetmealId, ids));
        // 封装成dto返回
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        String categoryName = categoryService.getById(setmeal.getCategoryId()).getName();
        setmealDto.setSetmealDishes(list);
        setmealDto.setCategoryName(categoryName);
        return setmealDto;
    }

    /**
     * 更新套餐
     * @param setmealDto
     */
    @Override
    @Transactional
    public void updateSetmeal(SetmealDto setmealDto) {
        // 将套餐信息保存
        saveOrUpdate(setmealDto);
        // 将套餐含有菜品信息保存
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(item->{
            item.setSetmealId(setmealDto.getId().toString());
            return item;
        }).collect(Collectors.toList());
        service.saveOrUpdateBatch(setmealDishes);
    }

    /**
     * 删除套餐
     * @param ids
     */
    @Override
    @Transactional
    public void delete(String ids) {
        // 删除套餐信息
        String[] idList = ids.split(",");
        for (String id : idList) {
            removeById(Long.parseLong(id));
            // 删除套餐中的菜品信息
            service.remove(new LambdaQueryWrapper<SetmealDish>().eq(SetmealDish::getSetmealId,id));
        }
    }
}
