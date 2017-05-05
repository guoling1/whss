package com.jkm.hss.product.servcie;

import com.jkm.hss.product.entity.PartnerRuleSetting;

import java.util.List;

/**
 * Created by xingliujie on 2017/5/4.
 */
public interface PartnerRuleSettingService {
    /**
     * 新增
     * @param partnerRuleSetting
     */
    void insert(PartnerRuleSetting partnerRuleSetting);

    /**
     * 根据产品编码查询升级规则
     * @return
     */
    List<PartnerRuleSetting> selectAllByProductId(long productId);
}
