package com.jkm.hss.bill.service;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.bill.entity.MerchantTradeResponse;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.helper.requestparam.QueryMerchantPayOrdersRequestParam;
import com.jkm.hss.merchant.helper.request.OrderTradeRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
public interface OrderService {

    /**
     * 插入
     *
     * @param order
     */
    void add(Order order);

    /**
     * 创建提现单
     *
     * @param payOrderId
     * @param merchantId
     * @param tradePeriod
     */
    long createPlayMoneyOrderByPayOrder(long payOrderId, long merchantId, String tradePeriod);

    /**
     * 更新
     *
     * @param order
     * @return
     */
    int update(Order order);

    /**
     * 更新交易备注
     *
     * @param id
     * @param remark
     */
    int updateRemark(long id, String remark);


    /**
     * 更新交易状态
     *
     * @param id
     * @param status
     */
    int updateStatus(long id, int status, String remark);

    /**
     * 更新结算状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateSettleStatus(long id, int status);

    /**
     * 结算成功
     *
     * @param id
     * @return
     */
    int markSettleSuccess(long id, int status, Date successSettleTime);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<Order> getById(long id);

    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    Optional<Order> getByIdWithLock(long id);

    /**
     * 按交易订单号查询
     *
     * @param orderNo
     * @param tradeType
     * @return
     */
    Optional<Order> getByOrderNoAndTradeType(String orderNo, int tradeType);

    /**
     * 根据支付单查询对应的打款单
     *
     * @param payOrderId
     * @return
     */
    Optional<Order> getByPayOrderId(long payOrderId);

    /**
     * 查询交易列表
     * @param orderRecord
     * @return
     */
    List<MerchantTradeResponse> selectOrderListByPage(OrderTradeRequest orderRecord);

    int selectOrderListCount(OrderTradeRequest orderRecord);

    String downloadExcel(OrderTradeRequest req, String baseUrl);

    /**
     * 交易详情
     * @param req
     * @return
     */
    MerchantTradeResponse selectOrderListByPageAll(OrderTradeRequest req);

    /**
     * 按交易订单号
     *
     * @param orderNo
     */
    Optional<Order> getByOrderNo(String orderNo);

    /**
     * 查询商户的收款单
     *
     * @param requestParam
     * @return
     */
    PageModel<Order> queryMerchantPayOrders(QueryMerchantPayOrdersRequestParam requestParam);

    /**
     * 按下游业务订单号查询
     *
     * @param businessOrderNo
     * @return
     */
    Optional<Order> getByBusinessOrderNo(String businessOrderNo);

    /**
     * 查询当前账户的交易总额
     *
     * @param accountId
     * @param appId
     * @param serviceType
     * @return
     */
    BigDecimal getTotalTradeAmountByAccountId(long accountId, String appId, int serviceType);

    /**
     * 查询交易详情
     * @param orderRecord
     * @return
     */
//    MerchantTradeResponse selectOrderListByPageAll(OrderListRequest orderRecord);
}
