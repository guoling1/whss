package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class JkmAccountRecord extends BaseEntity {

	/**
	*订单号
	*/
	private long orderId;

	/**
	*0.支付 1.提现
	*/
	private int tradeType;

	/**
	*金额(元)
	*/
	private BigDecimal totalFee;

}
