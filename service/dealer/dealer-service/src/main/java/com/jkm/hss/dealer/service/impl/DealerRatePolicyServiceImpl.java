package com.jkm.hss.dealer.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.dao.DealerRatePolicyDao;
import com.jkm.hss.dealer.entity.DealerRatePolicy;
import com.jkm.hss.dealer.service.DealerRatePolicyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xingliujie on 2017/6/8.
 */
@Slf4j
@Service
public class DealerRatePolicyServiceImpl implements DealerRatePolicyService {
    @Autowired
    private DealerRatePolicyDao dealerRatePolicyDao;
    /**
     * 新增
     *
     * @param dealerRatePolicy
     */
    @Override
    public void insert(DealerRatePolicy dealerRatePolicy) {
        dealerRatePolicyDao.insert(dealerRatePolicy);
    }

    /**
     * 修改
     *
     * @param dealerRatePolicy
     */
    @Override
    public void update(DealerRatePolicy dealerRatePolicy) {
        dealerRatePolicyDao.update(dealerRatePolicy);
    }

    /**
     * 根据代理商编码查询代理结算政策
     *
     * @param dealerId
     * @return
     */
    @Override
    public List<DealerRatePolicy> selectByDealerId(long dealerId) {
        return dealerRatePolicyDao.selectByDealerId(dealerId);
    }

    /**
     * 根据代理商编码查询代理结算政策个数
     *
     * @param dealerId
     * @return
     */
    @Override
    public Integer selectCountByDealerId(long dealerId) {
        return dealerRatePolicyDao.selectCountByDealerId(dealerId);
    }

    /**
     * 根据代理商编码查询代理结算政策
     *
     * @param dealerId
     * @param policyType
     * @return
     */
    @Override
    public Optional<DealerRatePolicy> selectByDealerIdAndPolicyType(long dealerId, String policyType) {
        return dealerRatePolicyDao.selectByDealerIdAndPolicyType(dealerId,policyType);
    }
}
