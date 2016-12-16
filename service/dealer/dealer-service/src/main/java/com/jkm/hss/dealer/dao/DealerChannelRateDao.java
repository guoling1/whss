package com.jkm.hss.dealer.dao;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-25.
 */

@Repository
public interface DealerChannelRateDao {


    /**
     * 初始化
     */
    void init(DealerChannelRate dealerChannelRate);

    /**
     * 更新
     *
     * @param dealerChannelRate
     * @return
     */
    int update(DealerChannelRate dealerChannelRate);

    /**
     * 查询
     *
     * @param dealerId
     * @param channelSignId
     * @return
     */
    List<DealerChannelRate> selectByDealerIdAndChannelId(@Param("dealerId") long dealerId, @Param("channelSignId") int channelSignId);

    /**
     * 根据代理商id 查询所有
     *
     * @param dealerId
     * @return
     */
    List<DealerChannelRate> selectByDealerId(@Param("dealerId") long dealerId);


    /**
     * 查询
     * @param dealerId
     * @param productId
     * @return
     */
    List<DealerChannelRate> selectByDealerIdAndProductId(@Param("dealerId") long dealerId, @Param("productId") long productId);

    /**
     * 按代理商id, 产品id, 通道类型查询
     *
     * @param dealerId
     * @param productId
     * @param channelType
     * @return
     */
    DealerChannelRate selectByDealerIdAndProductIdAndChannelType(@Param("dealerId") long dealerId, @Param("productId") long productId, @Param("channelType") int channelType);

    /**
     *
     * @param dealerId
     * @param payChannel
     * @return
     */
    List<DealerChannelRate> selectByDealerIdAndPayChannelSign(@Param("dealerId") long dealerId, @Param("payChannel") int payChannel);
}
