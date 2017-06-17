package com.jkm.hsy.user.help.requestparam;

import com.jkm.hsy.user.Enum.EnumPolicyType;
import com.jkm.hsy.user.entity.UserTradeRate;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xingliujie on 2017/6/9.
 */
@Data
public class UserTradeRateListRequest {
    /**
     * 主店编码
     */
    private long shopId;
    /**
     * 用户编码
     */
    private long userId;
    /**
     * 支付方式
     */
    private String rateName;
    /**
     * 政策类型
     * {@link EnumPolicyType}
     */
    private String policyType;
    /**
     * T1支付费率
     */
    private BigDecimal tradeRateT1;
    /**
     * D1支付费率
     */
    private BigDecimal tradeRateD1;
    /**
     * D0支付费率
     */
    private BigDecimal tradeRateD0;
}
