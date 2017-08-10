package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.HsyPartnerRules;
import org.springframework.stereotype.Repository;

/**
 * Created by xingliujie on 2017/8/9.
 */
@Repository
public interface HsyPartnerRulesDao{
    HsyPartnerRules selectTop1();
    void update(HsyPartnerRules hsyPartnerRules);
    void insert(HsyPartnerRules hsyPartnerRules);
}
