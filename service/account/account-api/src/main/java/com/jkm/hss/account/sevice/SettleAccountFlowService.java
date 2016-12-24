package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.SettleAccountFlow;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
public interface SettleAccountFlowService {

    /**
     * 插入
     *
     * @param accountFlow
     */
    void add(SettleAccountFlow accountFlow);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<SettleAccountFlow> getById(long id);

    /**
     * 按交易订单号查询
     *
     * @param orderNo
     * @return
     */
    Optional<SettleAccountFlow> getByOrderNo(String orderNo);
}
