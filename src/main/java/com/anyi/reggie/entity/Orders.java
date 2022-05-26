package com.anyi.reggie.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author anyi
 * @since 2022-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Orders对象", description="订单表")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "订单号")
    private String number;

    @ApiModelProperty(value = "订单状态 1待付款，2待派送，3已派送，4已完成，5已取消")
    private Integer status;

    @ApiModelProperty(value = "下单用户")
    private Long userId;

    @ApiModelProperty(value = "地址id")
    private Long addressBookId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "下单时间")
    private Date orderTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结账时间")
    private Date checkoutTime;

    @ApiModelProperty(value = "支付方式 1微信,2支付宝")
    private Integer payMethod;

    @ApiModelProperty(value = "实收金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "备注")
    private String remark;

    private String phone;

    private String address;

    private String userName;

    private String consignee;
}
