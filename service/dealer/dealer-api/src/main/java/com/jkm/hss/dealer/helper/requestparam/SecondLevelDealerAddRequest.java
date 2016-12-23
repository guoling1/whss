package com.jkm.hss.dealer.helper.requestparam;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/24.
 *
 * 添加二级代理商 入参
 */
@Data
public class SecondLevelDealerAddRequest {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 代理名称
     */
    private String name;

    /**
     * 所在地
     */
    private String belongArea;

    /**
     * 银行开户名称
     */
    private String bankAccountName;

    /**
     * 结算卡
     */
    private String bankCard;
    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行预留手机号
     */
    private String bankReserveMobile;

    /**
     * 微信结算价
     */
    private String weixinSettleRate;

    /**
     * 支付宝结算价
     */
    private String alipaySettleRate;

    /**
     * 无卡快捷结算价
     */
    private String quickSettleRate;

    /**
     * 提现结算价
     */
    private String withdrawSettleFee;
}
