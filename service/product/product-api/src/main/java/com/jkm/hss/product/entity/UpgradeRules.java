package com.jkm.hss.product.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Thinkpad on 2016/12/29.
 * 升级规则
 * tb_upgrade_rules
 */
@Data
public class UpgradeRules extends BaseEntity{
    /**
     * 产品编码
     */
    private long productId;
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
     * 须推广人数
     */
    private Integer promotionNum;

    /**
     * 升级费用
     */
    private BigDecimal upgradeCost;

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
     *直推升级费奖励
     */
    private BigDecimal directPromoteShall;

    /**
     *间推升级费奖励
     */
    private BigDecimal inDirectPromoteShall;
}
