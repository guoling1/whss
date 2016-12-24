package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.SettleAccountFlow;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Repository
public interface SettleAccountFlowDao {

    /**
     * 插入
     *
     * @param settleAccountFlow
     */
    void insert(SettleAccountFlow settleAccountFlow);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    SettleAccountFlow selectById(@Param("id") long id);

    /**
     * 按交易订单号查询
     *
     * @param orderNo
     * @return
     */
    SettleAccountFlow selectByOrderNo(@Param("orderNo") String orderNo);
}
