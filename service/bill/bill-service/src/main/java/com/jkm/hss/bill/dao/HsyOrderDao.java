package com.jkm.hss.bill.dao;

import com.jkm.base.common.util.Page;
import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hss.bill.entity.QueryHsyOrderRequest;
import com.jkm.hss.bill.entity.QueryHsyOrderResponse;
import com.jkm.hss.bill.helper.AppStatisticsOrder;
import com.jkm.hss.bill.helper.responseparam.HsyOrderSTResponse;
import com.jkm.hsy.user.entity.AppPolicyRechargeOrder;
import com.jkm.hss.bill.helper.responseparam.PcStatisticsOrder;
import com.jkm.hsy.user.entity.AppPolicyRechargeOrder;
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
     * 更新订单号
     *
     * @param id
     * @param orderNumber
     * @return
     */
    int updateOrderNumber(@Param("id") long id, @Param("orderNumber") String orderNumber);

    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    HsyOrder selectByIdWithLock(@Param("id") long id);

    /**
     * 订单列表-个数
     *
     * @param shopId
     * @param paymentChannels
     * @param merchantNo
     * @param selectAll
     * @param startTime
     * @param endTime
     * @return
     */
   long selectOrderCountByParam(@Param("shopId") long shopId,
                                @Param("merchantNo") String merchantNo,
                                @Param("tradeOrderNo") String tradeOrderNo,
                                @Param("selectAll") int selectAll,
                                @Param("paymentChannels") List<Integer> paymentChannels,
                                @Param("startTime") Date startTime,
                                @Param("endTime") Date endTime);


    /**
     * 订单列表-记录
     *
     * @param shopId
     * @param merchantNo
     * @param selectAll
     * @param paymentChannels
     * @param startTime
     * @param endTime
     * @return
     */
    List<HsyOrder> selectOrdersByParam(@Param("shopId") long shopId,
                                       @Param("merchantNo") String merchantNo,
                                       @Param("tradeOrderNo") String tradeOrderNo,
                                       @Param("selectAll") int selectAll,
                                       @Param("paymentChannels") List<Integer> paymentChannels,
                                       @Param("startTime") Date startTime,
                                       @Param("endTime") Date endTime,
                                       @Param("offset") int offset,
                                       @Param("count") int count);


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

    /**
     * 统计交易额度
     *
     * @param shopId
     * @param merchantNo
     * @param selectAll
     * @param paymentChannels
     * @param startTime
     * @param endTime
     * @return
     */
    AppStatisticsOrder statisticsOrdersByParam(@Param("shopId") long shopId,
                                               @Param("merchantNo") String merchantNo,
                                               @Param("selectAll") int selectAll,
                                               @Param("paymentChannels") List<Integer> paymentChannels,
                                               @Param("startTime") Date startTime,
                                               @Param("endTime") Date endTime);

    /**
     * pc统计
     *
     * @param shopId
     * @param startTime
     * @param endTime
     * @return
     */
    List<PcStatisticsOrder> pcStatisticsOrder(@Param("shopId") long shopId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    public List<HsyOrder> findConsumeOrderListByPage(Page<HsyOrder> entity);
    public Integer findConsumeOrderListByPageCount(HsyOrder entity);
    public List<HsyOrder> findConsumeOrderInfo(@Param("id")Long id);
    public List<AppPolicyRechargeOrder> findRechargeOrderInfoByOrderNO(@Param("orderNO")String orderNO);
    public List<AppPolicyRechargeOrder> findRechargeOrderInfoByID(@Param("id")Long id);

    /**
     * hsy订单
     * @param req
     * @return
     */
    List<QueryHsyOrderResponse> queryHsyOrderList(QueryHsyOrderRequest req);

    /**
     * hsy订单总数
     * @param req
     * @return
     */
    int queryHsyOrderListCount(QueryHsyOrderRequest req);

    /**
     * hsy订单支付金额统计
     * @param req
     * @return
     */
    String getHsyOrderCounts(QueryHsyOrderRequest req);

    /**
     * hsy订单支付手续费
     * @param req
     * @return
     */
    String getHsyOrderCounts1(QueryHsyOrderRequest req);

    /**
     * 下载hsy订单
     * @param req
     * @return
     */
    List<QueryHsyOrderResponse> selectHsyOrderList(QueryHsyOrderRequest req);
}
