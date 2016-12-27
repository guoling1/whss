package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.MerchantAndOrderRecord;
import com.jkm.hss.merchant.entity.OrderRecord;
import com.jkm.hss.merchant.entity.OrderRecordAndMerchant;
import com.jkm.hss.merchant.entity.OrderRecordConditions;
import com.jkm.hss.merchant.helper.request.RequestOrderRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-25 20:20
 */
@Repository
public interface OrderRecordDao {
    /**
     * 收款记录
     * @param req
     * @return
     */
    List<OrderRecord> selectAllOrderRecordByPage(RequestOrderRecord req);

    long selectAllOrderRecordCount(RequestOrderRecord req);

    /**
     * 余额明细
     * @param req
     * @return
     */
    List<OrderRecord> selectBalanceByPage(RequestOrderRecord req);
    long selectBalanceCount(@Param("merchantId") long merchantId);

    /**
     *
     * @param id
     * @return
     */
    OrderRecord selectByPrimaryKey(@Param("id") long id);

    /**
     *
     * @param orderId
     * @return
     */
    OrderRecord selectOrderId(@Param("orderId") String orderId,@Param("tradeType")int tradeType);

    /**
     *
     * @param orderRecord
     * @return
     */
    long insertSelective(OrderRecord orderRecord);

    int updateByPrimaryKeySelective(OrderRecord orderRecord);

    int updateParam(@Param("resultParams") String resultParams, @Param("outTradeNo") String outTradeNo, @Param("id") long id);

    List<OrderRecordConditions> selectOrderRecordByConditions(OrderRecordConditions orderRecord);

    List<OrderRecord> selectOtherPayByConditions();
    List<OrderRecord> selectPayByConditions();

    int selectOrderRecordByConditionsCount(OrderRecordConditions orderRecordConditions);


    /**
     * 提现
     * @param req
     * @return
     */
    List<OrderRecordAndMerchant> selectDrawWithRecordByPage(Map req);

    long selectDrawWithCount(Map req);

    List<MerchantAndOrderRecord> selectOrderList(Map map);

    int selectOrderListCount(Map map);

    MerchantAndOrderRecord selectOrderListCountAll(Map map);

    OrderRecordAndMerchant selectDrawWithRecordByPageAll(Map map);

    /**
     * 导出
     * @return
     */
    List<MerchantAndOrderRecord> selectOrderListTrade();

    MerchantAndOrderRecord selectProxyName(long firstLevel);
}
