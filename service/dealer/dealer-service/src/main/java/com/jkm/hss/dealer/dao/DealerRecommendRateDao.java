package com.jkm.hss.dealer.dao;

import com.jkm.hss.dealer.entity.DealerRecommendRate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
@Repository
public interface DealerRecommendRateDao {
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
    DealerRecommendRate selectById(@Param("id") long id);

    /**
     * 查询所有记录
     * @return
     */
    List<DealerRecommendRate> selectAll();
}
