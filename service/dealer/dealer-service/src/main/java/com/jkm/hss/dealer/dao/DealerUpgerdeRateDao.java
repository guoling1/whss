package com.jkm.hss.dealer.dao;

import com.jkm.hss.dealer.entity.DealerUpgerdeRate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
@Repository
public interface DealerUpgerdeRateDao {
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
    DealerUpgerdeRate selectById(@Param("id") long id);

    /**
     * 查询所有记录
     * @return
     */
    List<DealerUpgerdeRate> selectAll();


    /**
     * 根据代理商id和产品id查询记录
     * @param dealerId
     * @param productId
     * @return
     */
    List<DealerUpgerdeRate> selectByDealerIdAndProductId(@Param("dealerId") long dealerId,@Param("productId") long productId);

    /**
     *
     * @param dealerId
     * @param type @param productId
     * @return
     */
    DealerUpgerdeRate selectByDealerIdAndTypeAndProductId(@Param("dealerId") long dealerId, @Param("type") int type, @Param("productId") long productId);
}
