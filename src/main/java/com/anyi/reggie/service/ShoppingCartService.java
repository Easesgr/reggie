package com.anyi.reggie.service;

import com.anyi.reggie.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 购物车 服务类
 * </p>
 *
 * @author anyi
 * @since 2022-05-25
 */
public interface ShoppingCartService extends IService<ShoppingCart> {

    void add(ShoppingCart shoppingCart);

    List<ShoppingCart> getList();

    void sub(ShoppingCart shoppingCart);
}
