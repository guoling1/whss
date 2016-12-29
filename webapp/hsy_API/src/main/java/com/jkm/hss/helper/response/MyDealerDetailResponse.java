package com.jkm.hss.helper.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/29.
 */
@Data
public class MyDealerDetailResponse {

    /**
     * 代理商id
     */
    private long dealerId;

    /**
     *手机号
     */
    private String mobile;

    /**
     * 代理商名字
     */
    private String proxyName;

    /**
     * 配码数
     */
//    private int distributeCount;

    /**
     * 激活数
     */
//    private int activeCount;

    /**
     * 支付宝结算价
     */
    private String alipaySettleRate;

    /**
     * 微信结算价
     */
    private String weixinSettleRate;

    /**
     * 快捷结算价
     */
    private String quickSettleRate;

    /**
     * 提现结算价
     */
    private String withdrawSettleFee;

    /**
     * 二维码记录
     */
    private List<QRCodeRecord> codeRecords;

    @Data
    public class QRCodeRecord{
        /**
         * 个数
         */
        private int count;

        /**
         * 开始码号
         */
        private String startCode;

        /**
         * 结束码号
         */
        private String endCode;

        /**
         * 配码日期
         */
        private String distributeDate;
    }

}
