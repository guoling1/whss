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
public class UserWithdrawRate extends BaseEntity{
    /**
     * 用户编码
     */
    private long userId;
    /**
     * T1支付费率
     */
    private BigDecimal withdrawRateT1;
    /**
     * D1支付费率
     */
    private BigDecimal withdrawRateD1;
    /**
     * D0支付费率
     */
    private BigDecimal withdrawRateD0;

}
