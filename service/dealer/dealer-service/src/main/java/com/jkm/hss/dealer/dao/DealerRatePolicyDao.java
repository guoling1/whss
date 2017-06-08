package com.jkm.hss.dealer.dao;

import com.jkm.hss.dealer.entity.DealerRatePolicy;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xingliujie on 2017/6/8.
 */
@Repository
public interface DealerRatePolicyDao {
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
    List<DealerRatePolicy> selectByDealerId(@Param("dealerId") long dealerId);

    /**
     * 根据代理商编码查询代理结算政策个数
     * @param dealerId
     * @return
     */
    Integer selectCountByDealerId(@Param("dealerId") long dealerId);
}
