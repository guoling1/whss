package com.jkm.hss.bill.dao;

import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hss.bill.helper.responseparam.HsyOrderSTResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
     * 更新订单金额和状态
     *
     * @param id
     * @param amount
     */
    void updateAmountAndStatus(@Param("id") long id, @Param("amount") BigDecimal amount, @Param("status") int status);

    /**
     * 根据交易号查找订单
     * @return
     */
    HsyOrder selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据订单ID查找订单
     * @param id
     * @return
     */
    HsyOrder selectById(@Param("id") long id);

    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    HsyOrder selectByIdWithLock(@Param("id") long id);


   List<HsyOrder> selectOrdersByParam(@Param("shopId") long shopId,
                                      @Param("offset") int offset,
                                      @Param("count") int count,
                                      @Param("payChannelSigns") List<Integer> payChannelSigns,
                                      @Param("startTime") Date startTime,
                                      @Param("endTime") Date endTime);

   long selectOrderCountByParam(@Param("shopId") long shopId,
                                @Param("payChannelSigns") List<Integer> payChannelSigns,
                                @Param("startTime") Date startTime,
                                @Param("endTime") Date endTime);

    HsyOrderSTResponse selectDayOrderStByParm(@Param("shopId") long shopId,
                                           @Param("payChannelSigns") List<Integer> payChannelSigns,
                                           @Param("startTime") Date startTime,
                                           @Param("endTime") Date endTime);

   HsyOrderSTResponse selectOrderStByParm(@Param("shopId") long shopId,
                                          @Param("payChannelSigns") List<Integer> payChannelSigns,
                                          @Param("startTime") Date startTime,
                                          @Param("endTime") Date endTime);

    /**
     * 按商户编号和时间查询订单
     *
     * @param merchantNo
     * @param startTime
     * @param endTime
     * @return
     */
    List<HsyOrder> selectByMerchantNoAndTime(@Param("merchantNo") String merchantNo, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
