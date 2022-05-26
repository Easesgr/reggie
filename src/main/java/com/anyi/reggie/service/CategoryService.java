package com.anyi.reggie.service;

import com.anyi.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 菜品及套餐分类 服务类
 * </p>
 *
 * @author anyi
 * @since 2022-05-24
 */
public interface CategoryService extends IService<Category> {

    void delete(Long ids);
}
