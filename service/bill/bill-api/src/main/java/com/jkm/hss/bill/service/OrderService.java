package com.jkm.hss.bill.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.SplitAccountRefundRecord;
import com.jkm.hss.bill.entity.*;
import com.jkm.hss.bill.enums.EnumOrderRefundStatus;
import com.jkm.hss.bill.enums.EnumOrderStatus;
import com.jkm.hss.bill.helper.AppStatisticsOrder;
import com.jkm.hss.bill.helper.WithdrawParams;
import com.jkm.hss.bill.helper.requestparam.OrderBalanceStatistics;
import com.jkm.hss.bill.helper.requestparam.QueryMerchantPayOrdersRequestParam;
import com.jkm.hss.bill.helper.responseparam.PaymentSdkQueryPayOrderByOrderNoResponse;
import com.jkm.hss.bill.helper.responseparam.PaymentSdkQueryRefundOrderByOrderNoResponse;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.merchant.entity.GeTuiResponse;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.request.OrderTradeRequest;
import com.jkm.hsy.user.entity.AppBizCard;
import com.jkm.hsy.user.entity.AppBizShop;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
public interface OrderService {

    /**
     * 插入
     *
     * @param order
     */
    long add(Order order);

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
     * 创建提现单
     *
     * @param withdrawParams
     * @return
     */
    long createWithdrawOrder(WithdrawParams withdrawParams);

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
     * 更新退款信息
     *
     * @param id
     * @param refundAmount
     * @param refundStatus
     */
    int updateRefundInfo(long id, BigDecimal refundAmount, EnumOrderRefundStatus refundStatus);

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
     * 统计指定结算日期的交易
     *
     * @param settleDate
     * @return
     */
    List<OrderBalanceStatistics> statisticsPendingBalanceOrder(Date settleDate);

    /**
     * 查询交易列表
     * @param orderRecord
     * @return
     */
    List<MerchantTradeResponse> selectOrderListByPage(OrderTradeRequest orderRecord);

    int selectOrderListCount(OrderTradeRequest orderRecord);

    String downloadExcel(OrderTradeRequest req, String baseUrl);

    /**
     * 查询支付中心支付流水
     *
     * @param orderNo
     * @return
     */
    List<PaymentSdkQueryPayOrderByOrderNoResponse> queryPayOrderByOrderNo(String orderNo);

    /**
     * 查询支付中心退款流水
     *
     * @param refundOrderNo
     * @return
     */
    List<PaymentSdkQueryRefundOrderByOrderNoResponse> queryRefundOrderByOrderNo(String refundOrderNo);

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
     * 分页查询--查询个数version 1

     *
     * @param accountId
     * @param appId
     * @return
     */
    long getPageOrdersCountByAccountId(long accountId, String appId, Date date);

    /**
     * 分页查询--查询记录version 1
     *
     * @param accountId
     * @param appId
     * @param offset
     * @param count
     * @return
     */
    List<Order> getPageOrdersByAccountId(long accountId, String appId, int offset, int count, Date date);

    /**
     * 分页查询--查询个数
     *
     * @param accountId
     * @param appId
     * @param payChannelSigns
     * @param startTime
     * @param endTime
     * @return
     */
    long getOrderCountByParam(long accountId, String appId, List<Integer> payChannelSigns, Date startTime, Date endTime);

    /**
     * 分页查询--查询记录
     *
     * @param accountId
     * @param appId
     * @param offset
     * @param count
     * @param payChannelSigns
     * @param startTime
     * @param endTime
     * @return
     */
    List<Order> getOrdersByParam(long accountId, String appId, int offset, int count, List<Integer> payChannelSigns, Date startTime, Date endTime);

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
     *
     * @param channelList
     */
    void handleT1UnSettlePayOrder(List<Integer> channelList);

    /**
     * 查询指定结算日期，指定业务线，T1的支付成功的待结算的订单IDS
     *
     * @param settleDate
     * @param appId
     * @param channelList
     * @return
     */
    List<Long> getT1PaySuccessAndUnSettleOrderIds(Date settleDate, String appId, List<Integer> channelList);

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
     * 提现统计
     * @param req
     * @return
     */
    WithdrawResponse withdrawAmount(WithdrawRequest req);

    /**
     * 查询提现详情代理商
     * @param idd
     * @return
     */
    WithdrawResponse withdrawDetail(long idd,String createTimes);

    /**
     * 查询提现详情商户
     * @param idm
     * @return
     */
    WithdrawResponse withdrawDetails(long idm,String createTimes);

    /**
     * 查询打款
     * @param orderNo
     * @return
     */
    List<PlayResponse> getPlayMoney(String orderNo);

    /**
     * 代理商pc交易查询二级
     * @param req
     * @return
     */
    List<MerchantTradeResponse> getTrade(OrderTradeRequest req);

    /**
     * 代理商pc交易查询一级
     * @param req
     * @return
     */
    List<MerchantTradeResponse> getTradeFirst(OrderTradeRequest req);

    /**
     * 代理商pc交易查询二级总数
     * @param req
     * @return
     */
    int listCount(OrderTradeRequest req);

    /**
     * 代理商pc交易查询一级总数
     * @param req
     * @return
     */
    int listFirstCount(OrderTradeRequest req);

    /**
     * 下载提现
     * @param req
     * @param baseUrl
     * @return
     */
    String downLoad(WithdrawRequest req, String baseUrl);

    /**
     * 按订单号s统计交易金额
     *
     * @param orderNos
     * @return
     */
    Map<String, BigDecimal> getTradeAmountAndFeeByOrderNoList(List<String> orderNos);

    /**
     * 按订单号s 查询交易（app）
     *
     * @param orderNos
     * @param offset
     * @param pageSize
     * @return
     */
   List<Order> getOrderByOrderNos(List<String> orderNos, int offset, int pageSize);

    /**
     * 按结算单 查询交易（app）- 个数
     *
     * @param settlementRecordId
     * @return
     */
   int getOrderCountBySettlementRecordId(long settlementRecordId);

    /**
     * 按结算单 查询交易（app）- 记录
     *
     * @param settlementRecordId
     * @param offset
     * @param count
     * @return
     */
    List<Order> getOrderBySettlementRecordId(long settlementRecordId, int offset, int count);


    /**
     * 保存推送回调信息
     * @param geTuiResponse
     */
    void save(GeTuiResponse geTuiResponse);

    /**
     * 统计金额，笔数
     *
     * @param accountId
     * @param appId
     * @param payChannelSigns
     * @param sDate
     * @param eDate
     * @return
     */
    AppStatisticsOrder statisticsByParam(long accountId, String appId, ArrayList<Integer> payChannelSigns, String sDate, String eDate);

    /**
     * 查询退款单号
     * @param orderNo
     * @return
     */
    String getRefundOrder(String orderNo);

    /**
     * 根据订单号查询分润列表
     * @param orderNo
     * @return
     */
    List<ProfitRefundResponse> getProfitRefundList(String orderNo);

    /**
     * 分润退款记录
     * @param orderNo
     * @return
     */
    List<SplitAccountRefundRecord> splitAccountRefundList(String orderNo);

    /**
     * 按业务订单号查询个数
     *
     * @param businessOrderNo
     * @return
     */
    int getCountByBusinessOrder(String businessOrderNo);

    /**
     * T1 生成商户结算单后，将结算单id标记到交易中，并将交易标记为结算中
     *
     * @param settleDate
     * @param accountId
     * @param settlementRecordId
     * @param settleStatus
     * @param upperChannel
     * @return
     */
    int markOrder2SettlementIng(Date settleDate, long accountId, long settlementRecordId, int settleStatus, int upperChannel);

    /**
     * 按结算单批量更新结算状态
     *
     * @param settlementRecordId
     * @param settleStatus
     * @param oriSettleStatus
     * @return
     */
    int markOrder2SettlementSuccess(long settlementRecordId, int settleStatus, int oriSettleStatus);

    /**
     * 查询hss订单（订单查询）
     * @param req
     * @return
     */
    List<QueryOrderResponse> queryOrderList(QueryOrderRequest req);

    /**
     * 查询hss订单总数（订单查询）
     * @param req
     * @return
     */
    int queryOrderListCount(QueryOrderRequest req);

    /**
     * 统计订单金额
     * @param req
     * @return
     */
    String getOrderCount(QueryOrderRequest req);

    /**
     * 统计订单金额
     * @param req
     * @return
     */
    String getOrderCount1(QueryOrderRequest req);

    /**
     * 代理商交易统计金额
     * @param req
     * @return
     */
    String getAmountCount(OrderTradeRequest req);

    /**
     * 代理商交易统计金额
     * @param req
     * @return
     */
    String getAmountCount1(OrderTradeRequest req);

    /**
     * 业绩统计
     * @param req
     * @return
     */
    List<AchievementStatisticsResponse> getAchievement(QueryOrderRequest req);

    /**
     * 业绩统计总数
     * @param req
     * @return
     */
    int getAchievementCount(QueryOrderRequest req);

    /**
     * 导出业绩
     * @param req
     * @param baseUrl
     * @return
     */
    String downloadAchievement(QueryOrderRequest req, String baseUrl);

    /**
     * 代理商后台导出所属二级
     * @param req
     * @param baseUrl
     * @return
     */
    String downLoadHsyMerchantTrade(OrderTradeRequest req, String baseUrl);

    /**
     * 代理商后台导出一级代理商
     * @param req
     * @param baseUrl
     * @return
     */
    String downLoadHsyMerchantTrade1(OrderTradeRequest req, String baseUrl);

    /**
     * 代理商业务统计
     * @param req
     * @return
     */
    List<AchievementStatisticsResponse> getDaiLiAchievement(QueryOrderRequest req);

    /**
     * 代理商业务统计总数
     * @param req
     * @return
     */
    int getDaiLiAchievementCount(QueryOrderRequest req);

    /**
     * 导出代理商业务统计
     * @param req
     * @param baseUrl
     * @return
     */
    String downloadDaiLiAchievement(QueryOrderRequest req, String baseUrl);


    /**
     * Test
     *
     * @param strings
     * @param id
     */
    int updateSettlementRecordIdByOrderNos(List<String> strings, long id);

    /**
     * 分公司交易
     * @param req
     * @return
     */
    List<MerchantTradeResponse> getBranch(OrderTradeRequest req);

    int getBranchCount(OrderTradeRequest req);

    String downLoadBranch(OrderTradeRequest req, String baseUrl);

    /**
     * 分公司统计
     * @param req
     * @return
     */
    String getAmountCountBranch(OrderTradeRequest req);

    /**
     * 分公司统计
     * @param req
     * @return
     */
    String getAmountCountBranch1(OrderTradeRequest req);

    /**
     * 获取支付成功
     * @param count
     * @param status
     * @return
     */
    List<Order> selectOrderListByCount(long accountId, int count, EnumOrderStatus status, String payTime);

    /**
     * D0提现
     * @param account
     * @return
     */
    JSONObject d0WithDrawImpl(Account account, long userId, String merchantNo, AppBizCard appBizCard);

    /**
     * 确认提现
     * @param withDrawOrderId
     * @return
     */
    Pair<Integer, String> confirmWithdraw(long withDrawOrderId);

    /**
     * 二级代理商交易统计
     * @param req
     * @return
     */
    String getAmountCounts(OrderTradeRequest req);

    /**
     * 二级代理商交易统计
     * @param req
     * @return
     */
    String getAmountCounts1(OrderTradeRequest req);

    void markOrder2SettleFail(long settlementRecordId, int settleStatus, int oriSettleStatus);

    long selectWithdrawOrderCountByParam(long accountId);

    List<Order> selectWithdrawOrdersByParam(long accountId, int firstIndex, int pageSize);

    /**
     * 查询交易详情
     * @param orderRecord
     * @return
     */
//    MerchantTradeResponse selectOrderListByPageAll(OrderListRequest orderRecord);



}
