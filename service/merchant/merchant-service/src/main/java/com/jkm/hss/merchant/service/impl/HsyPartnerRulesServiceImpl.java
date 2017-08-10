package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.dao.HsyPartnerRulesDao;
import com.jkm.hss.merchant.entity.HsyPartnerRules;
import com.jkm.hss.merchant.service.HsyPartnerRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xingliujie on 2017/8/9.
 */
@Service
public class HsyPartnerRulesServiceImpl implements HsyPartnerRulesService {
    @Autowired
    private HsyPartnerRulesDao hsyPartnerRulesDao;
    @Override
    public HsyPartnerRules selectTop1() {
        return hsyPartnerRulesDao.selectTop1();
    }

    @Override
    public void update(HsyPartnerRules hsyPartnerRules) {
        hsyPartnerRulesDao.update(hsyPartnerRules);
    }

    @Override
    public void insert(HsyPartnerRules hsyPartnerRules) {
        hsyPartnerRulesDao.insert(hsyPartnerRules);
    }
}
