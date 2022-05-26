package com.anyi.reggie.dto;


import com.anyi.reggie.entity.OrderDetail;
import com.anyi.reggie.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private List<OrderDetail> orderDetails;
	
}
