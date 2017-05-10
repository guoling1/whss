package com.jkm.hss.product.servcie.impl;

import com.google.common.base.Optional;
import com.jkm.hss.product.dao.PartnerRuleSettingDao;
import com.jkm.hss.product.entity.PartnerRuleSetting;
import com.jkm.hss.product.helper.response.PartnerRuleSettingResponse;
import com.jkm.hss.product.servcie.PartnerRuleSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public List<PartnerRuleSettingResponse> selectAllByProductId(long productId) {
        List<PartnerRuleSettingResponse> partnerRuleSettingResponses = partnerRuleSettingDao.selectAllByProductId(productId);
        if(partnerRuleSettingResponses.size()>0){
            for(int i=0;i<partnerRuleSettingResponses.size();i++){
                if(partnerRuleSettingResponses.get(i).getBossRate()!=null){
                    partnerRuleSettingResponses.get(i).setBossRate(partnerRuleSettingResponses.get(i).getBossRate().multiply(new BigDecimal("100")));
                }
                if(partnerRuleSettingResponses.get(i).getDefaultProfitSpace()!=null){
                    partnerRuleSettingResponses.get(i).setDefaultProfitSpace(partnerRuleSettingResponses.get(i).getDefaultProfitSpace().multiply(new BigDecimal("100")));
                }
                if(partnerRuleSettingResponses.get(i).getClerkRate()!=null){
                    partnerRuleSettingResponses.get(i).setClerkRate(partnerRuleSettingResponses.get(i).getClerkRate().multiply(new BigDecimal("100")));
                }
                if(partnerRuleSettingResponses.get(i).getShopownerRate()!=null){
                    partnerRuleSettingResponses.get(i).setShopownerRate(partnerRuleSettingResponses.get(i).getShopownerRate().multiply(new BigDecimal("100")));
                }
                if(partnerRuleSettingResponses.get(i).getCommonRate()!=null){
                    partnerRuleSettingResponses.get(i).setCommonRate(partnerRuleSettingResponses.get(i).getCommonRate().multiply(new BigDecimal("100")));
                }
            }
        }
        return partnerRuleSettingResponses;
    }

    /**
     * 根据产品编码查询升级规则
     *
     * @param productId
     * @return
     */
    @Override
    public List<PartnerRuleSettingResponse> selectAll(long productId) {
        List<PartnerRuleSettingResponse> partnerRuleSettingResponses = partnerRuleSettingDao.selectAllByProductId(productId);
        return partnerRuleSettingResponses;
    }
}
