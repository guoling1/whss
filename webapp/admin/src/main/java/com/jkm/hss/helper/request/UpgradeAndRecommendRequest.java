package com.jkm.hss.helper.request;

import com.jkm.hss.product.entity.PartnerRuleSetting;
import com.jkm.hss.product.entity.UpgradeRules;
import com.jkm.hss.product.helper.request.UpgradeRulesRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Thinkpad on 2016/12/30.
 */
@Data
public class UpgradeAndRecommendRequest {
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
     * 升级规则
     */
    private List<PartnerRuleSetting> partnerRuleSettingList;
    /**
     *
     */
    private List<UpgradeRulesRequest> upgradeRulesRequestList;


}
