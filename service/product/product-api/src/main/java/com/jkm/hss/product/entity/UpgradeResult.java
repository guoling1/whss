package com.jkm.hss.product.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Thinkpad on 2016/12/29.
 */
@Data
public class UpgradeResult {
    /**
     * 主键id
     */
    protected long id;
    /**
     * 合伙人名称
     */
    private String name;

    /**
     * 合伙人类型
     * {@link com.jkm.hss.product.enums.EnumUpGradeType}
     */
    private Integer type;


    /**
     * 微信费率
     */
    private BigDecimal weixinRate;

    /**
     * 支付宝费率
     */
    private BigDecimal alipayRate;

    /**
     * 快捷费率
     */
    private BigDecimal fastRate;

    /**
     * 是否显示升级
     * {@link com.jkm.hss.product.enums.EnumIsUpGrade}
     */
    private int isUpgrade;
}
