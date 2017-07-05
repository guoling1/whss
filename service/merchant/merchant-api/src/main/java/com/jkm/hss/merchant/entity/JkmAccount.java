package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class JkmAccount extends BaseEntity {


	/**
	*累计总收入
	*/
	private BigDecimal totalIn;

	/**
	*累计可用总收入
	*/
	private BigDecimal totalAvailableIn;

	/**
	*余额
	*/
	private BigDecimal balance;

	/**
	*累计提现
	*/
	private BigDecimal totalWithdrawals;

}
