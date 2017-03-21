package com.jkm.hss.bill.service;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.bill.entity.MerchantTradeResponse;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.WithdrawRequest;
import com.jkm.hss.bill.entity.WithdrawResponse;
import com.jkm.hss.bill.helper.requestparam.QueryMerchantPayOrdersRequestParam;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.request.OrderTradeRequest;
import com.jkm.hsy.user.entity.AppBizShop;

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
     */
    long createPlayMoneyOrderByPayOrder(long payOrderId, long merchantId, String settleType);

    /**
     * 创建提现单
     *
     * @param shop
     * @param amount
     * @param appId
     * @param channel
     * @param settleType
     * @return
     */
    long createPlayMoneyOrder(AppBizShop shop, BigDecimal amount, String appId, int channel, String settleType);

    /**
     * 创建代理商提现单
     *
     * @param dealer
     * @param amount
     * @param appId
     * @param channel
     * @param settleType
     * @return
     */
    long createDealerPlayMoneyOrder(Dealer dealer, BigDecimal amount, String appId, int channel, String settleType, BigDecimal withdrawFee);

    /**
     * 创建商户提现单
     *
     * @param merchantInfo
     * @param amount
     * @param appId
     * @param channel
     * @param settleType
     * @return
     */
    long createMerchantPlayMoneyOrder(MerchantInfo merchantInfo, BigDecimal amount, String appId, int channel, String settleType, BigDecimal withdrawFee);

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
     * 查询已经对过账的订单号
     *
     * @param strings
     * @return
     */
    List<String> getCheckedOrderNosByOrderNos(List<String> strings);

    /**
     * 分页查询--查询个数

     *
     * @param accountId
     * @param appId
     * @return
     */
    long getPageOrdersCountByAccountId(long accountId, String appId, Date date);

    /**
     * 分页查询--查询记录
     *
     * @param accountId
     * @param appId
     * @param offset
     * @param count
     * @return
     */
    List<Order> getPageOrdersByAccountId(long accountId, String appId, int offset, int count, Date date);

    /**
     * 批量查询
     * @param orderNos
     * @return
     */
    List<Order> getByOrderNos(List<String> orderNos);

    /**
     * 一二级筛选用
     * @param req
     * @return
     */
    List<MerchantTradeResponse> getOrderList(OrderTradeRequest req);

    /**
     * 统计
     * @param req
     * @return
     */
    String amountCount(OrderTradeRequest req);

    /**
     * hss-T1-结算到卡定时处理实现
     */
    void handleT1UnSettlePayOrder();

    /**
     * 查询指定结算日期，指定业务线，T1的支付成功的待结算的订单IDS
     *
     * @param settleDate
     * @param appId
     * @return
     */
    List<Long> getT1PaySuccessAndUnSettleOrderIds(Date settleDate, String appId);

    /**
     * T1 按支付单ID，发起体现
     *
     * @param orderId
     */
    void t1WithdrawByOrderId(long orderId);

    List<WithdrawResponse> withdrawList(WithdrawRequest req);

    /**
     * 总数
     * @param req
     * @return
     */
    int getNo(WithdrawRequest req);
    /**
     * 查询交易详情
     * @param orderRecord
     * @return
     */
//    MerchantTradeResponse selectOrderListByPageAll(OrderListRequest orderRecord);
}
