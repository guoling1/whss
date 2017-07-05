package com.jkm.hsy.user.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hsy.user.Enum.EnumIsOpen;
import com.jkm.hsy.user.Enum.EnumPolicyType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/6/9.
 */
@Data
public class UserTradeRate extends BaseEntity{
    /**
     * 用户编码
     */
    private long userId;
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
    /**
     * 是否开通
     * {@link EnumIsOpen}
     */
    private int isOpen;

}
