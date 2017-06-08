package com.jkm.hss.dealer.service;

import com.jkm.hss.dealer.entity.DealerRatePolicy;

import java.util.List;

/**
 * Created by xingliujie on 2017/6/8.
 */
public interface DealerRatePolicyService {
    /**
     * 新增
     * @param dealerRatePolicy
     */
    void insert(DealerRatePolicy dealerRatePolicy);

    /**
     * 修改
     * @param dealerRatePolicy
     */
    void update(DealerRatePolicy dealerRatePolicy);

    /**
     * 根据代理商编码查询代理结算政策
     * @param dealerId
     * @return
     */
    List<DealerRatePolicy> selectByDealerId(long dealerId);

    /**
     * 根据代理商编码查询代理结算政策个数
     * @param dealerId
     * @return
     */
    Integer selectCountByDealerId(long dealerId);
}
