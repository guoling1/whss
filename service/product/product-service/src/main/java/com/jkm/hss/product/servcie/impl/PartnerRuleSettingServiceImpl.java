package com.jkm.hss.product.servcie.impl;

import com.jkm.hss.product.dao.PartnerRuleSettingDao;
import com.jkm.hss.product.entity.PartnerRuleSetting;
import com.jkm.hss.product.servcie.PartnerRuleSettingService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xingliujie on 2017/5/4.
 */
public class PartnerRuleSettingServiceImpl implements PartnerRuleSettingService {
    @Autowired
    private PartnerRuleSettingDao partnerRuleSettingDao;
    /**
     * 新增
     *
     * @param partnerRuleSetting
     */
    @Override
    public void insert(PartnerRuleSetting partnerRuleSetting) {
        partnerRuleSettingDao.insert(partnerRuleSetting);
    }
}
