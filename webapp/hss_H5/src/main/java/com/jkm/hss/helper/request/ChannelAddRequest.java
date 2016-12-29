package com.jkm.hss.helper.request;

import com.jkm.hss.product.enums.EnumBalanceTimeType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-11-29.
 */
@Data
public class ChannelAddRequest {

    /**
     * 通道名称(支付宝,微信)
     */
    private String channelName;
    /**
     * 收单公司(支付宝,微信)
     */
    private String thirdCompany;
    /**
     * 渠道来源(供应商名字:华友)
     */
    private String channelSource;
    /**
     * 支付费率
     */
    private BigDecimal basicTradeRate;
    /**
     * 提现成本费用
     */
    private BigDecimal basicWithdrawFee;
    /**
     * 结算时间
     * {@link EnumBalanceTimeType}
     */
    private String basicBalanceType;
}
