package com.jkm.hss.product.servcie.impl;

import com.google.common.base.Optional;
import com.jkm.hss.product.dao.PartnerRuleSettingDao;
import com.jkm.hss.product.entity.PartnerRuleSetting;
import com.jkm.hss.product.servcie.PartnerRuleSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xingliujie on 2017/5/4.
 */
@Service
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

    /**
     * 修改
     *
     * @param partnerRuleSetting
     */
    @Override
    public void update(PartnerRuleSetting partnerRuleSetting) {
        partnerRuleSettingDao.update(partnerRuleSetting);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Optional<PartnerRuleSetting> selectById(long id) {
        return Optional.fromNullable(partnerRuleSettingDao.selectById(id));
    }

    /**
     * 根据产品编码查询升级规则
     *
     * @param productId
     * @return
     */
    @Override
    public List<PartnerRuleSetting> selectAllByProductId(long productId) {
        return partnerRuleSettingDao.selectAllByProductId(productId);
    }
}
