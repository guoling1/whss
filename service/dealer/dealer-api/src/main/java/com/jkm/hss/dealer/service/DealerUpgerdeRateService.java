package com.jkm.hss.dealer.service;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.entity.DealerUpgerdeRate;
import com.jkm.hss.dealer.enums.EnumDealerRateType;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
public interface DealerUpgerdeRateService {
    /**
     * 初始化
     * @param dealerUpgerdeRate
     */
    int insert(DealerUpgerdeRate dealerUpgerdeRate);

    /**
     * 修改
     * @param dealerUpgerdeRate
     * @return
     */
    int update(DealerUpgerdeRate dealerUpgerdeRate);

    /**
     * 根据编码查询
     * @param id
     * @return
     */
    Optional<DealerUpgerdeRate> selectById(long id);

    /**
     * 查询所有记录
     * @return
     */
    List<DealerUpgerdeRate> selectAll();

    /**
     * 根据代理商id和产品id查询记录
     * @return
     */
    List<DealerUpgerdeRate> selectByDealerIdAndProductId(long dealerId,long productId);

    /**
     * 查询代理商的升级分润规则
     * @param dealerId
     * @param type
     * @param id
     * @return
     */
    DealerUpgerdeRate selectByDealerIdAndTypeAndProductId(long dealerId, EnumDealerRateType type, long id);
}
