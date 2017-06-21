package com.jkm.hss.bill.helper;

import lombok.Builder;
import lombok.Data;

/**
 * Created by yulong.zhang on 2017/5/3.
 */
@Data
@Builder
public class PlaceOrderParams {
    /**
     * 系统商户号
     */
    private String merchantNo;
    /**
     * 前端回调地址
     */
    private String returnUrl;
    /**
     * 后台回调地址
     */
    private String notifyUrl;
    /**
     * 微信appId
     */
    private String wxAppId;
    /**
     * 用户在微信公众号的openId或则在支付宝的唯一标识
     */
    private String memberId;

    /**
     * 子商户公众账号ID（服务商）
     */
    private String subAppId;

    /**
     * 子商户号（服务商）
     */
    private String subMerchantId;

    /**
     * 用户子标识（服务商）
     */
    private String subMemberId;

    //###################HSS-特殊渠道-例如卡盟################
    /**
     * 收款行联行号
     */
    private String bankBranchCode;

    /**
     * 入账卡号 DES加密
     */
    private String bankCardNo;

    /**
     * 入帐卡对应姓名
     *
     */
    private String realName;

    /**
     * 入帐卡对应身份证号 DES加密
     *
     */
    private String idCard;

    /**
     * 结算成功，回调url
     */
    private String settleNotifyUrl;
}
