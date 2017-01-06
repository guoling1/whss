package com.jkm.hss.dealer.service;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.entity.DealerRecommendRate;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
public interface DealerRecommendRateService {
    /**
     * 初始化
     * @param dealerRecommendRate
     */
    int insert(DealerRecommendRate dealerRecommendRate);

    /**
     * 修改
     * @param dealerRecommendRate
     * @return
     */
    int update(DealerRecommendRate dealerRecommendRate);

    /**
     * 根据编码查询
     * @param id
     * @return
     */
    Optional<DealerRecommendRate> selectById(long id);

    /**
     * 查询所有记录
     * @return
     */
    List<DealerRecommendRate> selectAll();
}
