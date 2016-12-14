package com.jkm.hss.merchant.entity;
import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
public class PayExceptionRecord extends BaseEntity {

	/**
	 * 商户订单id
	 */
	private String orderId;
	/**
	 * fastpay|alipay|weixin
	 */
	private String payChannel;
	/**
	 * 支付流水号
	 */
	private String reqSn;

	/**
	* E异常 U处理中
	*/
	private String type;

}
