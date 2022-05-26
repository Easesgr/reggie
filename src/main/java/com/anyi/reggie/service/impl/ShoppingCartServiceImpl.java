package com.anyi.reggie.service.impl;

import com.anyi.reggie.common.UserContext;
import com.anyi.reggie.entity.ShoppingCart;
import com.anyi.reggie.mapper.ShoppingCartMapper;
import com.anyi.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author anyi
 * @since 2022-05-25
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    /**
     * 添加购物车项
     * @param shoppingCart
     */
    @Override
    public void add(ShoppingCart shoppingCart) {
        // 获取当前用户id
        Long userId = UserContext.getUserId();
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(shoppingCart.getDishId()!=null, ShoppingCart::getDishId,shoppingCart.getDishId());
        wrapper.eq(shoppingCart.getSetmealId()!=null, ShoppingCart::getSetmealId,shoppingCart.getSetmealId());

        ShoppingCart one = getOne(wrapper);
        // 根据 套餐id 获取 菜品id查询数据库是否已有
        if (one != null){
            // 已有就+1
            shoppingCart.setId(one.getId());
            shoppingCart.setNumber(one.getNumber() +1);
            updateById(shoppingCart);
        }else{
            // 没有就添加
            save(shoppingCart);
        }
    }

    /**
     * 查询购物车
     * @return
     */
    @Override
    public List<ShoppingCart> getList() {
        // 获取用户id
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ShoppingCart::getCreateTime);
        wrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> list = list(wrapper);
        return list;
    }

    /**
     * 删除购物车项
     * @param shoppingCart
     */
    @Override
    public void sub(ShoppingCart shoppingCart) {
        Long userId = UserContext.getUserId();
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(shoppingCart.getDishId()!=null, ShoppingCart::getDishId,shoppingCart.getDishId());
        wrapper.eq(shoppingCart.getSetmealId()!=null, ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        ShoppingCart one = getOne(wrapper);

        if (one.getNumber() == 1){
            // 如果只有一个就把他删掉
            removeById(one.getId());
        }else {
            // 否则就减1
            shoppingCart.setId(one.getId());
            shoppingCart.setNumber(one.getNumber() -1);
            updateById(shoppingCart);
        }
    }
}
