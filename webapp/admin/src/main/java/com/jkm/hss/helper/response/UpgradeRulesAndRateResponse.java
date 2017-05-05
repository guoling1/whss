package com.jkm.hss.helper.response;

import com.jkm.hss.product.entity.PartnerRuleSetting;
import com.jkm.hss.product.entity.UpgradeRules;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Thinkpad on 2016/12/30.
 */
@Data
public class UpgradeRulesAndRateResponse {
    /**
     * 商户升级规则
     */
    private List<PartnerRuleSetting> PartnerRuleSettingList;
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

}
