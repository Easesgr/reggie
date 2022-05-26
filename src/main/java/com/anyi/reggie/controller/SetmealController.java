package com.anyi.reggie.controller;


import com.anyi.reggie.common.R;
import com.anyi.reggie.dto.SetmealDto;
import com.anyi.reggie.entity.Dish;
import com.anyi.reggie.entity.Setmeal;
import com.anyi.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author anyi
 * @since 2022-05-24
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Resource
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R add(@RequestBody SetmealDto setmealDto){
        setmealService.add(setmealDto);
        return R.success("新城套餐成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R getPage(Integer page,Integer pageSize,String name){
        Page<SetmealDto> pageInfo = setmealService.getPage(page,pageSize,name);
        return R.success(pageInfo);
    }

    /**
     * 根据id查询套餐
     * @param ids
     * @return
     */
    @GetMapping("/{ids}")
    public R getSetmeal(@PathVariable Long ids){
        SetmealDto setmealDto = setmealService.getSetmeal(ids);
        return R.success(setmealDto);
    }

    /**
     * 更新套餐信息
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R updateSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.updateSetmeal(setmealDto);
        return R.success("更新套餐成功!");
    }

    /**
     * 修改销售状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R changeStatus(@PathVariable int status,String ids){
        String[] idList = ids.split(",");
        for (String id : idList) {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(Long.parseLong(id));
            setmeal.setStatus(status);

            setmealService.updateById(setmeal);
        }
        return R.success("更新状态成功");
    }

    @DeleteMapping
    public R delete(String  ids){
        setmealService.delete(ids);
        return R.success("删除套餐成功！");
    }
    @GetMapping("/list")
    public R getList(Long categoryId,Integer status){
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getStatus,status).eq(Setmeal::getCategoryId,categoryId);
        List<Setmeal> list = setmealService.list(wrapper);
        return R.success(list);
    }

    @GetMapping("/dish/{id}")
    public R getSetMeal(@PathVariable Long id){
        Setmeal setmeal = setmealService.getById(id);
        return R.success(setmeal);
    }

}

