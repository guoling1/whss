package com.jkm.hss.bill.dao;

import com.jkm.hss.bill.entity.MerchantTradeResponse;
import com.jkm.hss.bill.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Repository
public interface OrderDao {

    /**
     * 插入
     *
     * @param order
     */
    void insert(Order order);

    /**
     * 更新
     *
     * @param order
     * @return
     */
    int update(Order order);

    /**
     * 更改交易备注
     *
     * @param id
     * @param remark
     * @return
     */
    int updateRemark(@Param("id") long id, @Param("remark") String remark);

    /**
     * 更新交易状态
     *
     * @param id
     * @param status
     */
    int updateStatus(@Param("id") long id, @Param("status") int status, @Param("status") String remark);

    /**
     * 更新结算状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateSettleStatus(@Param("id") long id, @Param("status") int status);

    /**
     * 结算成功
     *
     * @param id
     * @return
     */
    int markSettleSuccess(@Param("id") long id, @Param("status") int status, @Param("successSettleTime") Date successSettleTime);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Order selectById(@Param("id") long id);

    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    Order selectByIdWithLock(@Param("id") long id);

    /**
     * 按交易订单号查询
     *
     * @param orderNo
     * @param tradeType
     * @return
     */
    Order selectByOrderNoAndTradeType(@Param("orderNo") String orderNo, @Param("tradeType") int tradeType);

    /**
     * 根据支付单id查询打款单
     *
     * @param payOrderId
     * @return
     */
    Order selectByPayOrderId(@Param("payOrderId") long payOrderId);

    /**
     * 交易列表
     * @param map
     * @return
     */
    List<MerchantTradeResponse> selectOrderList(Map map);

    /**
     * 总页数
     * @param map
     * @return
     */
    int selectOrderListCount(Map map);

    /**
     * 交易详情
     * @param map
     * @return
     */
    MerchantTradeResponse selectOrderListCountAll(Map map);

    /**
     * 查询商户信息
     * @param payee
     * @param payer
     * @return
     */
    List<MerchantTradeResponse> getMerchant(@Param("payee") long payee,@Param("payer") long payer);

    /**
     * 查询代理商
     * @param dealerId
     * @return
     */
    List<MerchantTradeResponse> getDealer(@Param("dealerId") long dealerId);

    /**
     * 查询firstLevelDealerId
     * @param firstLevelDealerId
     * @return
     */
    List<MerchantTradeResponse> getProxyName(long firstLevelDealerId);
}
