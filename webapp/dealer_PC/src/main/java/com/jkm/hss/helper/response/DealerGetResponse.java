package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/24.
 */
@Data
public class DealerGetResponse {

    private long dealerId;

    /**
     * 代理手机号
     */
    private String mobile;

    /**
     * 代理名称(唯一)
     */
    private String proxyName;

    /**
     * 所属区域
     */
    private String belongArea;

    /**
     * 银行开户名
     */
    private String bankAccountName;

    /**
     * 银行卡号
     */
    private String bankCard;

    /**
     * 银行预留手机号
     */
    private String bankReserveMobile;

    /**
     * 支付宝结算价
     */
    private String alipaySettleRate;

    /**
     * 微信结算价
     */
    private String weixinSettleRate;

    /**
     * 商户结算价
     */
    private String merchantSettleRate;

    /**
     * 快捷结算价
     */
    private String quickSettleRate;

    /**
     * 提现结算价
     */
    private String withdrawSettleFee;

    /**
     * 商户提现结算价
     */
    private String merchantWithdrawSettleFee;
}
