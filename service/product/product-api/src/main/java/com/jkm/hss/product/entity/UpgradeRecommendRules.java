package com.jkm.hss.product.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Thinkpad on 2016/12/30.
 * 升级推荐分润设置
 * tb_upgrade_recommend_rules
 */
@Data
public class UpgradeRecommendRules extends BaseEntity {
    /**
     * 产品编码
     */
    private long productId;
    /**
     * 邀请达标标准
     */
    private BigDecimal inviteStandard;
    /**
     *升级费分润
     */
    private BigDecimal upgradeRate;

    /**
     *收单分润
     */
    private BigDecimal tradeRate;


}
