package com.jkm.hss.dealer.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.dao.DealerUpgerdeRateDao;
import com.jkm.hss.dealer.entity.DealerUpgerdeRate;
import com.jkm.hss.dealer.service.DealerUpgerdeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
@Service
public class DealerUpgerdeRateServiceImpl implements DealerUpgerdeRateService{
    @Autowired
    private DealerUpgerdeRateDao dealerUpgerdeRateDao;
    /**
     * 初始化
     *
     * @param dealerUpgerdeRate
     */
    @Override
    public int insert(DealerUpgerdeRate dealerUpgerdeRate) {
        return dealerUpgerdeRateDao.insert(dealerUpgerdeRate);
    }

    /**
     * 修改
     *
     * @param dealerUpgerdeRate
     * @return
     */
    @Override
    public int update(DealerUpgerdeRate dealerUpgerdeRate) {
        return dealerUpgerdeRateDao.update(dealerUpgerdeRate);
    }

    /**
     * 根据编码查询
     *
     * @param id
     * @return
     */
    @Override
    public Optional<DealerUpgerdeRate> selectById(long id) {
        return Optional.fromNullable(dealerUpgerdeRateDao.selectById(id));
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    @Override
    public List<DealerUpgerdeRate> selectAll() {
        return dealerUpgerdeRateDao.selectAll();
    }

    /**
     * 根据代理商id和产品id查询记录
     *
     * @param dealerId
     * @param productId
     * @return
     */
    @Override
    public List<DealerUpgerdeRate> selectByDealerIdAndProductId(long dealerId, long productId) {
        return dealerUpgerdeRateDao.selectByDealerIdAndProductId(dealerId,productId);
    }
}
