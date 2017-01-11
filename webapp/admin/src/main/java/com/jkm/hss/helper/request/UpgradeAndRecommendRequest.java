package com.jkm.hss.helper.request;

import com.jkm.hss.product.entity.UpgradeRules;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Thinkpad on 2016/12/30.
 */
@Data
public class UpgradeAndRecommendRequest {
    /**
     * 升级级别
     */
    private Integer type;

    /**
     * 产品编码
     */
    private long productId;
    /**
     * 邀请用户达标标准
     */
    private BigDecimal standard;
    /**
     * 升级费分润
     */
    private BigDecimal upgradeRate;
    /**
     * 收单分润
     */
    private BigDecimal tradeRate;
    /**
     * 收单奖励分润池
     */
    private BigDecimal rewardRate;
    /**
     * 升级规则
     */
    private List<UpgradeRules> upgradeRulesList;


}
