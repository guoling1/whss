package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.HsyPartnerRules;

/**
 * Created by xingliujie on 2017/8/9.
 */
public interface HsyPartnerRulesService {
    HsyPartnerRules selectTop1();
    void update(HsyPartnerRules hsyPartnerRules);
    void insert(HsyPartnerRules hsyPartnerRules);
}
