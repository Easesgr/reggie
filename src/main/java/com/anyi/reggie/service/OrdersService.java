package com.anyi.reggie.service;

import com.anyi.reggie.dto.OrdersDto;
import com.anyi.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author anyi
 * @since 2022-05-25
 */
public interface OrdersService extends IService<Orders> {

    void addOrders(Orders orders);

    Page<OrdersDto> userPage(Integer page, Integer pageSize);
}
