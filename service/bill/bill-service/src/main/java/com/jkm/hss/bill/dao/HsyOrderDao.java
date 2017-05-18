package com.jkm.hss.bill.dao;

import com.jkm.hss.bill.entity.HsyOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by wayne on 17/5/16.
 */
@Repository
public interface HsyOrderDao {
    /**
     * 插入
     * @param hsyOrder
     */
    void insert(HsyOrder hsyOrder);

    /**
     * 更新
     * @param hsyOrder
     * @return
     */
    int update(HsyOrder hsyOrder);

    /**
     * 根据交易号查找订单
     * @return
     */
    HsyOrder selectByOrderNo(@Param("orderNo") String orderNo);
}
