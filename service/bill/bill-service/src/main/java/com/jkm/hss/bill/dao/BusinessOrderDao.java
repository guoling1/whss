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
     * 更新订单号
     *
     * @param orderNo
     * @param id
     * @return
     */
    int updateOrderNoById(@Param("orderNo") String orderNo, @Param("id") long id);

    /**
     * 更新备注
     *
     * @param remark
     * @param orderNo
     * @return
     */
    int updateRemarkByOrderNo(@Param("remark") String remark, @Param("orderNo") String orderNo);

    /**
     * 更新状态
     *
     * @param status
     * @param orderNo
     * @return
     */
    int updateStatusByOrderNo(@Param("status") int status, @Param("orderNo") String orderNo);

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
