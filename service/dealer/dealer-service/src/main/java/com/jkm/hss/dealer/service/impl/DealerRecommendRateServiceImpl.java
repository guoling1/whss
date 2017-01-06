package com.jkm.hss.dealer.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.dao.DealerRecommendRateDao;
import com.jkm.hss.dealer.entity.DealerRecommendRate;
import com.jkm.hss.dealer.service.DealerRecommendRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
@Service
public class DealerRecommendRateServiceImpl implements DealerRecommendRateService{
    @Autowired
    private DealerRecommendRateDao dealerRecommendRateDao;
    /**
     * 初始化
     *
     * @param dealerRecommendRate
     */
    @Override
    public int insert(DealerRecommendRate dealerRecommendRate) {
        return dealerRecommendRateDao.insert(dealerRecommendRate);
    }

    /**
     * 修改
     *
     * @param dealerRecommendRate
     * @return
     */
    @Override
    public int update(DealerRecommendRate dealerRecommendRate) {
        return dealerRecommendRateDao.update(dealerRecommendRate);
    }

    /**
     * 根据编码查询
     *
     * @param id
     * @return
     */
    @Override
    public Optional<DealerRecommendRate> selectById(long id) {
        return Optional.fromNullable(dealerRecommendRateDao.selectById(id));
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    @Override
    public List<DealerRecommendRate> selectAll() {
        return dealerRecommendRateDao.selectAll();
    }
}
