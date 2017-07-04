package com.jkm.hss.merchant.entity;
import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Data
public class PastRecord extends BaseEntity {


	/**
	*交易单号
	*/
	private String pastNo;

	/**
	*商户号
	*/
	private long accounId;

	/**
	*
	*/
	private long tradeId;

	/**
	*订单号
	*/
	private long orderId;

	/**
	*支付金额(元)
	*/
	private BigDecimal totalFee;

	/**
	*D0,T0,T1
	*/
	private String settlePeriod;

	/**
	*结算状态 0,已经算 1.未结算
	*/
	private int settleStatus;

	/**
	*1 交易 2.提现
	*/
	private int tradeType;


}
