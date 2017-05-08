package com.jkm.hss.product.helper.response;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Thinkpad on 2016/12/29.
 * 升级规则
 * tb_upgrade_rules
 */
@Data
public class UpgradeRulesResponse{
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
     * 邀请升级达标人数
     */
    private Integer promotionNum;

    /**
     * 升级费用
     */
    private BigDecimal upgradeCost;

    /**
     *直推升级费奖励
     */
    private BigDecimal directPromoteShall;

    /**
     *间推升级费奖励
     */
    private BigDecimal inDirectPromoteShall;
}
