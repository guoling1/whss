package com.jkm.hss.merchant.helper.response;

import lombok.Data;

/**
 * Created by xingliujie on 2017/2/27.
 */
@Data
public class MerchantChannelRateResponse {
    /**
     * 是否有支行
     *1.有
     * 2.无
     */
    private int isBranch;
    /**
     * 是否有信用卡
     *1.有
     * 2.无
     */
    private int isCreditCard;
    /**
     * 是否入网
     * 1.入网申请成功
     * 2.入网失败
     */
    private int isNet;
    /**
     * 入网返回信息
     */
    private String message;
}
