package com.jkm.hss.bill.dao;

import com.jkm.hss.bill.entity.BusinessOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by yulong.zhang on 2017/5/18.
 */
@Repository
public interface BusinessOrderDao {

    /**
     * 插入
     *
     * @param businessOrder
     */
    void insert(BusinessOrder businessOrder);

    /**
     * 更新
     *
     * @param businessOrder
     * @return
     */
    int update(BusinessOrder businessOrder);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    BusinessOrder selectById(@Param("id") long id);

    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    BusinessOrder selectByIdWithLock(@Param("id") long id);

    /**
     * 按订单号查询
     *
     * @param orderNo
     * @return
     */
    BusinessOrder selectByOrderNo(@Param("orderNo") String orderNo);
}
